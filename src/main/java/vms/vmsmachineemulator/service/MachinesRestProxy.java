package vms.vmsmachineemulator.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vms.vmsmachineemulator.configuration.ApiParams;
import vms.vmsmachineemulator.configuration.SensorsParams;
import vms.vmsmachineemulator.dto.MachineDTO;
import vms.vmsmachineemulator.dto.MachineFrontDTO;
import vms.vmsmachineemulator.dto.SensorDTO;
import vms.vmsmachineemulator.dto.SensorTypeEnum;
import vms.vmsmachineemulator.repo.StateMongoRepository;
import vms.vmsmachineemulator.service.interfaces.IMachines;
import vms.vmsmachineemulator.service.interfaces.ISensors;
import vms.vmsmachineemulator.util.UtilService;

@Service
public class MachinesRestProxy implements IMachines {
  private RestTemplate rest = new RestTemplate();
  @Autowired
  StateMongoRepository stateRepo;
  @Autowired
  ISensors sensorService;
  @Autowired
  SensorsParams sensorsParams;
  @Autowired
  ApiParams apiParams;

  @Override
  public List<MachineDTO> getMachines() {
    List<MachineFrontDTO> machines = this.getMachinesFront();
    List<MachineDTO> machinesDTO = this.getMachineSensorValues(machines);
    return machinesDTO;

  }

  /**
   * Converts collection of machines data, received from frontend server to
   * MachineDTO;
   * 
   * @param machinesFront
   * @return
   */
  private List<MachineDTO> getMachineSensorValues(List<MachineFrontDTO> machinesFront) {
    return machinesFront.stream().map(this::getMachinesDTOWithSensorsValue).collect(Collectors.toList());
  }

  /**
   * Converts machine data, received from frontend to the MachineDTO, sets proper
   * sensor value; If state of given machine does not exists returns MachineDTO
   * with default sensor values;
   */
  private MachineDTO getMachinesDTOWithSensorsValue(MachineFrontDTO machine) {
    MachineDTO machineState = this.stateRepo.findById(machine.getMachineId()).orElse(null);
    if (machineState == null) {
      return this.getDefaultMachineState(machine);
    }
    List<SensorDTO> sensors = machineState.getSensors().stream()
        .filter(
            stateSensor -> isStateSensorExistsInMachine(machine.getProductSensor().keySet(), stateSensor.getSensorId()))
        .collect(Collectors.toList());
    return new MachineDTO(machine.getMachineId(), sensors);
  }

  /**
   * Checks that given sensorId exists between registered machine sensors;
   * 
   * @param sensorIds
   * @param sensorId
   * @return
   */
  private boolean isStateSensorExistsInMachine(Set<Integer> productsIds, Integer sensorId) {
    Set<Integer> nonProductsIds = this.sensorsParams.getSensorsDesc().stream()
    .map(desc -> desc.getSensorId())
    .collect(Collectors.toSet());
    Set<Integer> sensorsIds = UtilService.mergeSets(productsIds, nonProductsIds);
    return sensorsIds.contains(sensorId);
  }

  /**
   * Returns new MachineDTO with MachineFrontDTO data, and default values on
   * registered sensors;
   * 
   * @param machine
   * @return
   */
  private MachineDTO getDefaultMachineState(MachineFrontDTO machine) {
    List<SensorDTO> sensorsProducts = machine.getProductSensor().keySet().stream()
        .map(sensorId -> getSensorDTOWIthDefaultValue(machine.getMachineId(), sensorId)).collect(Collectors.toList());
    List<SensorDTO> sensorsNonProducts = this.getNonProductsSensors(machine.getMachineId());
    List<SensorDTO> sensors = UtilService.mergeLists(sensorsProducts, sensorsNonProducts);
    return new MachineDTO(machine.getMachineId(), sensors);
  }

  /**
   * Returns list of non product sensors with default values;
   * 
   * @param machineId
   * @return
   */
  List<SensorDTO> getNonProductsSensors(int machineId) {
    return this.sensorsParams.getSensorsDesc().stream().map(sensorDesc -> {
      int sensorId = sensorDesc.getSensorId();
      return this.getSensorDTOWIthDefaultValue(machineId, sensorId);
    }).collect(Collectors.toList());
  }

  /**
   * Returns new SensorDTO with given machineId, sensorId and default value,
   * calculated by given sensor id;
   */
  private SensorDTO getSensorDTOWIthDefaultValue(Integer machineId, Integer sensorId) {
    SensorTypeEnum sensorType = this.sensorService.getSensorType(sensorId);
    int sensorValue = this.sensorService.getDefaultSensorValue(sensorType);
    return new SensorDTO(machineId, sensorId, sensorValue);
  }

  private List<MachineFrontDTO> getMachinesFront() {
    String reqURL = this.apiParams.getRoot() + "/util/machine/all";
    ResponseEntity<List<MachineFrontDTO>> response = rest.exchange(reqURL, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<MachineFrontDTO>>() {
        });
    return response.getBody();
  }
}