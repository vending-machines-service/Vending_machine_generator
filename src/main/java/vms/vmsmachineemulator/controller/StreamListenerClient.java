// package vms.vmsmachineemulator.controller;

// import java.io.IOException;

// import org.springframework.cloud.stream.annotation.EnableBinding;

// import vms.vmsmachineemulator.dto.SensorDTO;
// import vms.vmsmachineemulator.service.interfaces.IDispatcher;

// import com.fasterxml.jackson.core.JsonParseException;
// import com.fasterxml.jackson.databind.JsonMappingException;
// import com.fasterxml.jackson.databind.ObjectMapper;


// @EnableBinding(IDispatcher.class)
// public class StreamListenerClient {

// 	private static final long TIME_DELAY = 1;
// 	ObjectMapper mapper = new ObjectMapper();
// 	int n = 1;
	
// 	// @StreamListener(IDispatcher.INPUT)
// 	void receiveSensorData(String jsonData) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
// 		SensorDTO sensor = mapper.readValue(jsonData, SensorDTO.class);
// 		System.out.printf("#%d, machineId=%d, sensorId=%d, value=%d\n", n++, sensor.getMachineId(), sensor.getMachineId(), sensor.getValue());
// 		Thread.sleep(TIME_DELAY);
// 	}
// }
