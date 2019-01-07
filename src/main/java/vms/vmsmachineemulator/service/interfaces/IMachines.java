package vms.vmsmachineemulator.service.interfaces;

import java.util.List;

import vms.vmsmachineemulator.dto.MachineDTO;

public interface IMachines {
  public List<MachineDTO> getMachines();
}