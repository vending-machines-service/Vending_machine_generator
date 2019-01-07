package vms.vmsmachineemulator.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vms.vmsmachineemulator.configuration.EmulatorParams;
import vms.vmsmachineemulator.configuration.SensorProps;
import vms.vmsmachineemulator.dto.SensorStorage;
import vms.vmsmachineemulator.dto.SensorTypeEnum;
import vms.vmsmachineemulator.service.interfaces.ISensors;
import vms.vmsmachineemulator.util.UtilService;

@Service
@Slf4j
public class SensorsService implements ISensors {

  @Autowired
  EmulatorParams params;
  
  @Override
  public SensorTypeEnum getSensorType(int sensorId) {
    Map<String, SensorProps> ranges = this.params.getSensorRanges();
    if (sensorId >= ranges.get("DECREASE").getFrom() && sensorId < ranges.get("DECREASE").getTo()) {
      return SensorTypeEnum.DECREASE;
    } else if (sensorId >= ranges.get("CRASH").getFrom() && sensorId < ranges.get("CRASH").getTo()) {
      return SensorTypeEnum.CRASH;
    } else if (sensorId >= ranges.get("INCREASE").getFrom() && sensorId < ranges.get("INCREASE").getTo()) {
      return SensorTypeEnum.INCREASE;
    } else {
      return SensorTypeEnum.NOT_MAPPED;
    }
  }

  public void calculateNextValue(SensorStorage sensorStorage) {
    if (sensorStorage.getType() == SensorTypeEnum.INCREASE) {
      this.calculateIncSensorNextValue(sensorStorage);
    } else if (sensorStorage.getType() == SensorTypeEnum.DECREASE) {
      this.calculateDecSensorNextValue(sensorStorage);
    } else if(sensorStorage.getType() == SensorTypeEnum.CRASH) {
      this.calculateCrashNextValue(sensorStorage);
    } else {
      log.error("SENSOR {}:{} IS OUT OF RANGE", sensorStorage.getSensor().getMachineId(), sensorStorage.getSensor().getSensorId());
    }
  }

  private void calculateIncSensorNextValue(SensorStorage sensorStorage) {
    int currValue = sensorStorage.getSensor().getValue();
    int delta = UtilService.getRandomNumber(0, this.params.getDeltaMax());
    int newValue = currValue + delta;
    newValue = newValue <= 100 ? newValue : 100;
    sensorStorage.getSensor().setValue(newValue);
  }

  private void calculateDecSensorNextValue(SensorStorage sensorStorage) {
    int currValue = sensorStorage.getSensor().getValue();
    int delta = UtilService.getRandomNumber(0, this.params.getDeltaMax());
    int newValue = currValue - delta;
    newValue = newValue >= 0 ? newValue : 0;
    sensorStorage.getSensor().setValue(newValue);
  }

  private void calculateCrashNextValue(SensorStorage sensorStorage) {
    if (sensorStorage.getSensor().getValue() == 1) return;
    boolean isCrash = UtilService.getRandomNumber(0, 100) < this.params.getCrashProbability();
    sensorStorage.getSensor().setValue(isCrash ? 1 : 0);
  }
}