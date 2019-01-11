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
public class MachineFrontDTO {

  private int machineId;
  private Map<Integer, Integer> productSensor;

}
