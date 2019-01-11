package vms.vmsmachineemulator.service.interfaces;

public interface IEmulator {
  public boolean startTest();
  public boolean stopTest();
  public boolean resetMachine(int machineId);
}