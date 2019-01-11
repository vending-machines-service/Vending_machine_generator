package vms.vmsmachineemulator.service.interfaces;

public interface IMachineEmulator {
  /**
   * Emits current machine sensors values into Kafka's sensors topic and 
   * calculates next sensors values;
   */
  public void emitStepSensorData();
  /**
   * Reset machine's sensors values to default;
   */
  public boolean reset();
}