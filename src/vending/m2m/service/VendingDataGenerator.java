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
	@Value("${max_value:5}")
	int maxValue;
	int machineId = 1;
	int value = 100;
	int coins = 0;
	int cash = 0;
	int nSensors = 0;
	@InboundChannelAdapter(value = Source.OUTPUT,
			poller = @Poller(
					fixedDelay = "${fixedDelay:6000}",
					maxMessagesPerPoll = "${nMessages:18}"))
	String getSensorData() throws JsonProcessingException {
		VendingSensorData sensor = null;
		int number = getRandomNumber(minValue, maxValue);
		if (number == 1 && value>0) {
			value -= 10;
		}
		nSensors += 1;
		if(nSensors<17) {
			sensor = getSensor(machineId, nSensors, value);
		}
		if(nSensors == 17) {
			coins = (100 - value)/10 * 3;
			sensor = getSensor(machineId, nSensors, coins);
		}
		else if (nSensors == 18) {
			cash = coins/2;
			sensor = getSensor(machineId, nSensors, cash);
		}
		if(nSensors > 18) {
			nSensors = 0;
		}
		
		return mapper.writeValueAsString(sensor);
	}
	private VendingSensorData getSensor(int machineId,int nSensors,int value) {
		
		return new VendingSensorData(machineId, nSensors, value);
		}
	
	private int getRandomNumber(int min, int max) {
		return random.ints(1, min, max+1).findFirst().getAsInt();
	}
}
