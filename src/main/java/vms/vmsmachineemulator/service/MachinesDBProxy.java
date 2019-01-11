package vms.vmsmachineemulator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import vms.vmsmachineemulator.configuration.EmulatorParams;
import vms.vmsmachineemulator.dto.MachineDTO;
import vms.vmsmachineemulator.dto.SensorDTO;
import vms.vmsmachineemulator.dto.SensorTypeEnum;
import vms.vmsmachineemulator.dto.StartMachineDTO;
import vms.vmsmachineemulator.entity.MachineJPA;
import vms.vmsmachineemulator.entity.MachineProductSensorJPA;
import vms.vmsmachineemulator.repo.MachinesSqlRepository;
import vms.vmsmachineemulator.service.interfaces.IMachines;

@ConfigurationProperties("vms")
// @Service
public class MachinesDBProxy implements IMachines {

	@Autowired
	private EmulatorParams params;
	@Autowired
	MachinesSqlRepository SQLRepo;

	private int incSensorCount;
	private int decSensorCount;
	private int crashSensorCount;

	@Override
	public List<MachineDTO> getMachines() {
		List<StartMachineDTO> startMachines = getStartMachineDTO();
		return getMachines(startMachines);
	}

	private List<StartMachineDTO> getStartMachineDTO() {
		return SQLRepo.findAll().stream().map(mach -> convertJPAtoDTO(mach)).collect(Collectors.toList());
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

	private StartMachineDTO convertJPAtoDTO(MachineJPA machineJpa) {

		int machineId = machineJpa.machineId;
		String firmName = machineJpa.firmName;
		String location = machineJpa.location;
		Map<Integer, Integer> productSensor = new HashMap<>();
		for (MachineProductSensorJPA set : machineJpa.getProducts()) {
			productSensor.put(set.getSensorId(), set.getProductId());
		}

		return new StartMachineDTO(machineId, firmName, location, productSensor);
	}
}