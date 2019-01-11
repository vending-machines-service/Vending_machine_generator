package vms.vmsmachineemulator.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SensorDTO {
  Integer machineId;
  Integer sensorId;
  Integer value;

  public SensorDTO(Integer machineId, Integer sensorId, Integer value) {
    this.machineId = machineId;
    this.sensorId = sensorId;
    this.value = value;
  }
}