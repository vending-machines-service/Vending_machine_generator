package vms.vmsmachineemulator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import vms.vmsmachineemulator.service.EmulatorService;

@RestController
public class MachineEmulatorController {
  @Autowired
  EmulatorService emulator;
  
  @GetMapping("/start")
  void startEmulation() {
    emulator.startTest();
  }

  @GetMapping("/stop")
  void stopEmulation() {
    emulator.stopTest();
  }

  @GetMapping("/complete/{machineId}")
  boolean restoreMachine(@PathVariable("machineId") int machineId) {
    return false;
  }

  @GetMapping("/reset-machine/{machineId}")
  void resetMachine(@PathVariable("machineId") int machineId) {
    this.emulator.resetMachine(machineId);
  }
}