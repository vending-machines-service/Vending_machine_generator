package vms.vmsmachineemulator.dto;

import java.util.Map;

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
public class StartMachineDTO {

  public int machineId;
  public String firmName;
  public String location;
  public Map<Integer, Integer> productSensor;
  

  public StartMachineDTO(int machineId, String firmName, String location, Map<Integer, Integer> productSensor) {
    super();
    this.machineId = machineId;
    this.firmName = firmName;
    this.location = location;
    this.productSensor = productSensor;
  	}
}