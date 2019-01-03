package vending.m2m.service;

import java.io.IOException;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import vending.m2m.dto.SensorDTO;

@EnableBinding(Sink.class)
public class VendingReceiverSensor {

	private static final long TIME_DELAY = 1;
	ObjectMapper mapper = new ObjectMapper();
	int n = 1;
	
	@StreamListener(Sink.INPUT)
	void receiveSensorData(String jsonData) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		SensorDTO sensor = mapper.readValue(jsonData, SensorDTO.class);
		System.out.printf("#%d, machineId=%d, sensorId=%d, value=%d\n", n++, sensor.machineId, sensor.sensorId, sensor.value);
		Thread.sleep(TIME_DELAY);
	}
}
