package vending.m2m.dto;

public class VendingSensorData {

	public int machineId;
	public int sensorId;
	public int value;
	
	public int getMachineId() {
		return machineId;
	}

	public int getSensorId() {
		return sensorId;
	}

	public int getValue() {
		return value;
	}

	public VendingSensorData(int machineId, int sensorId, int value) {
		super();
		this.machineId = machineId;
		this.sensorId = sensorId;
		this.value = value;
	}

	public VendingSensorData(){
		
	}

}
