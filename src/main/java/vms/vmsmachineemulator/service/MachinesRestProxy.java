package vms.vmsmachineemulator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vms.vmsmachineemulator.configuration.EmulatorParams;
import vms.vmsmachineemulator.dto.MachineDTO;
import vms.vmsmachineemulator.dto.SensorDTO;
import vms.vmsmachineemulator.dto.SensorTypeEnum;
import vms.vmsmachineemulator.dto.StartMachineDTO;
import vms.vmsmachineemulator.service.interfaces.IMachines;

@ConfigurationProperties("vms.sensorCount")
// @Service
public class MachinesRestProxy implements IMachines {

	@Autowired
	private EmulatorParams params;
	private int incSensorCount;
	private int decSensorCount;
	private int crashSensorCount;
	private String URL;

	@Override
	public List<MachineDTO> getMachines() {
		List<StartMachineDTO> startMachines = getStartMachineDTO();
		return getMachines(startMachines);
	}

	private List<StartMachineDTO> getStartMachineDTO() {
		RestTemplate rt = new RestTemplate();
		return rt.exchange(URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<StartMachineDTO>>() {}).getBody();
		
	}

	private List<MachineDTO> getMachines(List<StartMachineDTO> startMachines) {
		List<MachineDTO> machines = new ArrayList<MachineDTO>();
		for (int i = 0; i < startMachines.size(); i++) {
			MachineDTO machine = createMachine(startMachines.get(i).machineId, incSensorCount, decSensorCount,
					crashSensorCount);
			machines.add(machine);
		}
		return machines;
	}

	public MachineDTO createMachine(int machineId, int incSensorCount, int decSensorCount, int crashSensorCount) {
		List<SensorDTO> sensors = new ArrayList<SensorDTO>();
		addIncreaseSensors(sensors, incSensorCount, machineId);
		addDecreaseSensors(sensors, decSensorCount, machineId);
		addCrashSensors(sensors, crashSensorCount, machineId);
		return new MachineDTO(machineId, sensors);
	}
	
	public void addIncreaseSensors(List<SensorDTO> sensors, int sensorCount, int machineId) {
		int beginIndex = this.params.getSensorRanges().get(SensorTypeEnum.INCREASE.toString()).getFrom();
		int endIndex = beginIndex + sensorCount;
		int maxIndex = this.params.getSensorRanges().get(SensorTypeEnum.INCREASE.toString()).getTo();
		for (int i = beginIndex; i < endIndex && i < maxIndex; i++) {
			SensorDTO sensor = new SensorDTO(machineId, i, 0);
			sensors.add(sensor);
		}
	}

	public void addDecreaseSensors(List<SensorDTO> sensors, int sensorCount, int machineId) {
		int beginIndex = this.params.getSensorRanges().get(SensorTypeEnum.DECREASE.toString()).getFrom();
		int endIndex = beginIndex + sensorCount;
		int maxIndex = this.params.getSensorRanges().get(SensorTypeEnum.DECREASE.toString()).getTo();
		for (int i = beginIndex; i < endIndex && i < maxIndex; i++) {
			SensorDTO sensor = new SensorDTO(machineId, i, this.params.getMaxValue());
			sensors.add(sensor);
		}
	}

	public void addCrashSensors(List<SensorDTO> sensors, int sensorCount, int machineId) {
		int beginIndex = this.params.getSensorRanges().get(SensorTypeEnum.CRASH.toString()).getFrom();
		int endIndex = beginIndex + sensorCount;
		int maxIndex = this.params.getSensorRanges().get(SensorTypeEnum.CRASH.toString()).getTo();
		for (int i = beginIndex; i < endIndex && i < maxIndex; i++) {
			SensorDTO sensor = new SensorDTO(machineId, i, 0);
			sensors.add(sensor);
		}
	}
}