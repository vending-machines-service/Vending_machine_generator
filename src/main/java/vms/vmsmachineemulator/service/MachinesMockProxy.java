package vms.vmsmachineemulator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vms.vmsmachineemulator.configuration.EmulatorParams;
import vms.vmsmachineemulator.dto.MachineDTO;
import vms.vmsmachineemulator.dto.SensorDTO;
import vms.vmsmachineemulator.dto.SensorTypeEnum;
import vms.vmsmachineemulator.service.interfaces.IMachines;

@Service
public class MachinesMockProxy implements IMachines {

  @Autowired
  private EmulatorParams params;

  @Override
  public List<MachineDTO> getMachines() {
    return getMachines(1, 1, 1, 1);
  }
  
  public List<MachineDTO> getMachines (int machinesCount, int incSensorCount, int decSensorCount, int crashSensorCount) {
    List<MachineDTO> machines = new ArrayList<MachineDTO>();
    for (int i = 0; i < machinesCount; i++) {
      MachineDTO machine = createMachine(i, incSensorCount, decSensorCount, crashSensorCount);
      machines.add(machine);
    }
    return machines;
  }

  public MachineDTO createMachine(int machineId, int incSensorCount, int decSensorCount, int crashSensorCount) {
    List<SensorDTO> sensors = new ArrayList<SensorDTO>();
    addIncreaseSensors(sensors, incSensorCount, machineId);
    addDecreaseSensors(sensors, decSensorCount, machineId);
    addCrashSensors(sensors, crashSensorCount, machineId);
    return new MachineDTO(machineId, sensors);
  }

  public void addIncreaseSensors(List<SensorDTO> sensors, int sensorCount, int machineId) {
    int beginIndex = this.params.getSensorRanges().get(SensorTypeEnum.INCREASE.toString()).getFrom();
    int endIndex = beginIndex + sensorCount;
    int maxIndex = this.params.getSensorRanges().get(SensorTypeEnum.INCREASE.toString()).getTo();
    for (int i = beginIndex; i < endIndex && i < maxIndex; i++) {
      SensorDTO sensor = new SensorDTO(machineId, i, 0);
      sensors.add(sensor);
    }
  }
  public void addDecreaseSensors(List<SensorDTO> sensors, int sensorCount, int machineId) {
    int beginIndex = this.params.getSensorRanges().get(SensorTypeEnum.DECREASE.toString()).getFrom();
    int endIndex = beginIndex + sensorCount;
    int maxIndex = this.params.getSensorRanges().get(SensorTypeEnum.DECREASE.toString()).getTo();
    for (int i = beginIndex; i < endIndex && i < maxIndex; i++) {
      SensorDTO sensor = new SensorDTO(machineId, i, this.params.getMaxValue());
      sensors.add(sensor);
    }
  }
  public void addCrashSensors(List<SensorDTO> sensors, int sensorCount, int machineId) {
    int beginIndex = this.params.getSensorRanges().get(SensorTypeEnum.CRASH.toString()).getFrom();
    int endIndex = beginIndex + sensorCount;
    int maxIndex = this.params.getSensorRanges().get(SensorTypeEnum.CRASH.toString()).getTo();
    for (int i = beginIndex; i < endIndex && i < maxIndex; i++) {
      SensorDTO sensor = new SensorDTO(machineId, i, 0);
      sensors.add(sensor);
    }
  }
}