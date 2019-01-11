package vms.vmsmachineemulator.service;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.support.MessageBuilder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import vms.vmsmachineemulator.configuration.EmulatorParams;
import vms.vmsmachineemulator.dto.MachineDTO;
import vms.vmsmachineemulator.dto.SensorDTO;
import vms.vmsmachineemulator.dto.SensorStorage;
import vms.vmsmachineemulator.dto.SensorTypeEnum;
import vms.vmsmachineemulator.service.interfaces.IDispatcher;
import vms.vmsmachineemulator.service.interfaces.IMachineEmulator;
import vms.vmsmachineemulator.service.interfaces.ISensors;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@EnableBinding(IDispatcher.class)
public class MachineEmulator extends Thread implements IMachineEmulator {

  ApplicationContext applicationContext;
  EmulatorParams params;
  ISensors sensorsService;
  IDispatcher dispatcher;
  ObjectMapper mapper = new ObjectMapper();
  private boolean isEmulationContinues = true;
  private int machineId;
  private Set<SensorStorage> sensors;

  public MachineEmulator(MachineDTO machine, ApplicationContext appContext) {
    this.applicationContext = appContext;
    this.sensorsService = this.applicationContext.getBean(ISensors.class);
    this.dispatcher = this.applicationContext.getBean(IDispatcher.class);
    this.params = this.applicationContext.getBean(EmulatorParams.class);
    this.sensors = new HashSet<SensorStorage>();
    machine.getSensors().forEach(this::addSensor);
  }

  @Override
  public void run() {
    while (true && this.isEmulationContinues) {
      this.emitStepSensorData();
      try {
        sleep(this.params.getSendInterval());
      } catch (InterruptedException e) {
        log.error("MACHINE {} EMULATION STOPPED", this.machineId);
        Thread.currentThread().interrupt();
      }
    }
  }

  @Override
  public void emitStepSensorData() {
    this.sensors.forEach(this::emitAndCalculateNext);
  }

  @Override
  public boolean reset() {
    this.sensors.forEach(this::resetSensor);
    return false;
  }

  public void stopEmulator() {
    this.isEmulationContinues = false;
  }
  /**
   * Wraps sensor data into SensorStorage object with sensor type;
   * 
   * @param sensor
   */
  private void addSensor(SensorDTO sensor) {
    SensorTypeEnum type = this.sensorsService.getSensorType(sensor.getSensorId());
    SensorStorage sensorStorage = new SensorStorage(sensor, type);
    this.sensors.add(sensorStorage);
  }

  /**
   * Emits given sensor data into Kafka and calculates next sensor value;
   * 
   * @param sensor
   */
  private void emitAndCalculateNext(SensorStorage sensor) {
    this.sendData(sensor);
    this.sensorsService.calculateNextValue(sensor);
  }

  /**
   * Sends sensor data into sensors kafka chennel; If sensor value equals -1(which
   * means that sensor is not responding) does not do anything;
   */
  void sendData(SensorStorage sensor) {
    if (sensor.getSensor().getValue() == -1) {
      return;
    }
    try {
      String message = this.mapper.writeValueAsString(sensor.getSensor());
      this.dispatcher.sendSensorData().send(MessageBuilder.withPayload(message).build());
      log.info("=> MACHINE {}; SENSOR {}; VALUE {} SENT", sensor.getSensor().getMachineId(),
          sensor.getSensor().getSensorId(), sensor.getSensor().getValue());
    } catch (JsonProcessingException e) {
      log.error("Json Processing Exception in machine: {}", this.machineId);
    }
  }
  /**
   * Set default value into given sensor object, calculated by sensor type;
   * @param sensorStorage
   */
  private void resetSensor(SensorStorage sensorStorage) {
    SensorDTO sensor = sensorStorage.getSensor();
    int value = this.sensorsService.getDefaultSensorValue(sensorStorage.getType());
    sensor.setValue(value);
  }
}