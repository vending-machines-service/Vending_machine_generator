package vending.m2m.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import vending.m2m.dto.VendingSensorData;

@EnableBinding(Source.class)
public class VendingDataGenerator {
	
	ObjectMapper mapper = new ObjectMapper();
	Random random = new Random();
	@Value("${n_sensors:18}")
	int nSensors;
	
	@InboundChannelAdapter(value = Source.OUTPUT,
			poller = @Poller(
					fixedDelay = "${fixedDelay:60000}", 
					maxMessagesPerPoll = "${nMessages:18}"))
	String getSensorData() throws JsonProcessingException {
		VendingSensorData sensor = getSensor();
		return mapper.writeValueAsString(sensor);
	}
	
	private VendingSensorData getSensor() {
		int sensorId =0;
		int value = 0;
		return new VendingSensorData(nSensors, sensorId, value);
		}
	
}
