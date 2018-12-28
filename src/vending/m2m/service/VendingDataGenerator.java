package vending.m2m.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import vending.m2m.dto.VendingSensorData;

@EnableBinding(Source.class)
public class VendingDataGenerator {
	
	ObjectMapper mapper = new ObjectMapper();
	Random random = new Random();
	@Value("${min_value:0}")
	int minValue;
	@Value("${max_value:1}")
	int maxValue;
	int machineId = 1;
	int value = 100;
	int nSensors = 18;
	@InboundChannelAdapter(value = Source.OUTPUT,
			poller = @Poller(
					fixedDelay = "${fixedDelay:6000}",
					maxMessagesPerPoll = "${nMessages:18}"))
	String getSensorData() throws JsonProcessingException {

		int number = getRandomNumber(minValue, maxValue);
		if (number == 1 && value>0) {
			value -= 10;
			
		}
		VendingSensorData sensor = getSensor(machineId, getRandomNumber(1, nSensors), value);
		return mapper.writeValueAsString(sensor);
	}
	private VendingSensorData getSensor(int machineId,int nSensors,int value) {
		
		return new VendingSensorData(machineId, nSensors, value);
		}
	
	private int getRandomNumber(int min, int max) {
		return random.ints(1, min, max+1).findFirst().getAsInt();
	}
}
