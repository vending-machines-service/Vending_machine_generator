package vms.vmsmachineemulator.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.slf4j.SLF4JLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import vms.vmsmachineemulator.dto.MachineDTO;
import vms.vmsmachineemulator.service.interfaces.IEmulator;
import vms.vmsmachineemulator.service.interfaces.IMachines;

@Service
public class EmulatorService implements IEmulator {
  @Autowired
  private ApplicationContext applicationContext;
  @Autowired
  IMachines machinesService;
  Logger log = LoggerFactory.getLogger(SLF4JLogger.class);

  Map<Integer, MachineEmulator> machineEmulators;

  public EmulatorService() {
    this.machineEmulators = new HashMap<Integer, MachineEmulator>();
  }

  @Override
  public boolean startTest() {
    this.prepareMachineEmulators();
    if (this.machineEmulators.size() == 0)
      return false;
    this.machineEmulators.values().forEach(this::startEmulation);
    return true;
  }

  @Override
  public boolean stopTest() {
    if (this.machineEmulators.size() == 0)
      return false;
    this.machineEmulators.values().forEach(this::stopEmulation);
    return true;
  }

  @Override
  public boolean resetMachine(int machineId) {
    MachineEmulator emulator = this.machineEmulators.get(machineId);
    if (emulator == null) {
      return false;
    }
    emulator.reset();
    return true;
  }

  @Override
  public boolean prepareMachineEmulators() {
    List<MachineDTO> machines = this.machinesService.getMachines();
    if (machines.size() == 0)
      return false;
    machines.forEach(this::addMachineEmulator);
    return true;
  }

  private void startEmulation(MachineEmulator emulator) {
    emulator.start();
    this.log.info("EMULATOR OF MACHINE ID={} STARTED", emulator.getMachineId());
  }

  private void stopEmulation(MachineEmulator emulator) {
    emulator.interrupt();;
    this.log.info("EMULATOR OF MACHINE ID={} STOPPED", emulator.getMachineId());
  }

  private void addMachineEmulator(MachineDTO machine) {
    MachineEmulator emulator = new MachineEmulator(machine, this.applicationContext);
    this.machineEmulators.put(machine.getMachineId(), emulator);
  }
}