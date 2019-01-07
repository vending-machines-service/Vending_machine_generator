package vms.vmsmachineemulator.dto;

import java.util.List;

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
public class MachineDTO {

  public int machineId;
  public List<SensorDTO> sensors;

  public MachineDTO(int machineId, List<SensorDTO> sensors) {
    super();
    this.machineId = machineId;
    this.sensors = sensors;
  }
}
