package vms.vmsmachineemulator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vms.vmsmachineemulator.dto.MachineDTO;
import vms.vmsmachineemulator.service.interfaces.IMachines;

// @Service
public class MachinesRestProxy implements IMachines {

  @Override
  public List<MachineDTO> getMachines() {
    return null;
  }
}