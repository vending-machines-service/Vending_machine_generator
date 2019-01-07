package vms.vmsmachineemulator.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
public class SensorStorage {
  private SensorDTO sensor;
  private SensorTypeEnum type;

  public SensorStorage(SensorDTO sensor, SensorTypeEnum type) {
    this.sensor = sensor;
    this.type = type;
  }
}