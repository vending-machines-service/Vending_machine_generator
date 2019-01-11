package vms.vmsmachineemulator.service.interfaces;

import vms.vmsmachineemulator.dto.SensorStorage;
import vms.vmsmachineemulator.dto.SensorTypeEnum;

public interface ISensors {
  public SensorTypeEnum getSensorType(int sensorId);
  public void calculateNextValue(SensorStorage sensorStorage);
  public int getDefaultSensorValue(SensorTypeEnum type);
}