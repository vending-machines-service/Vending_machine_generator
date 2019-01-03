package vending.m2m.service;

import java.util.Random;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import vending.m2m.dto.SensorDTO;

@EnableBinding(Source.class)
public class VendingDataGenerator {

	private static final int N_PRODUCT_SENSORS = 5;
	private static final int N_MONEY_SENSORS = 2;
	private static final int SALE_PROBABILITY = 20;
	private static final int MONEY_SENSOR_INDEX = 200;
	private static final int CRASH_SENSOR_INDEX = 600;
	private static final int CRASH_PROBABILITY = 1;
	private static final int N_CRASH_SENSORS = 10;
	int machineId = 1;
	int[] productValue = new int[N_PRODUCT_SENSORS];
	int[] money = new int[N_MONEY_SENSORS];
	int[] crashBoolean = new int[N_CRASH_SENSORS];
	int nMoneySensor = 0;
	int nProductSensor = 0;
	int nCrashSensor = 0;
	int currentMoney = 0;
	ObjectMapper mapper = new ObjectMapper();
	Random random = new Random();

	
	@InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "${pFixedDelay:3000}", maxMessagesPerPoll = "${pnMessages:5}"))
	String getProductSensorData() throws JsonProcessingException {
		boolean sale = getRandomNumber(1, 100 / SALE_PROBABILITY) == 1 ? true : false;
		if (sale && productValue[nProductSensor] < 100) {
			productValue[nProductSensor] += 10;
		}
		SensorDTO sensor = new SensorDTO(machineId, nProductSensor, 100 - productValue[nProductSensor]);
		nProductSensor++;
		if (nProductSensor >= N_PRODUCT_SENSORS) {
			nProductSensor = 0;
		}
		return mapper.writeValueAsString(sensor);
	}

	@InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "${mFixedDelay:3000}", maxMessagesPerPoll = "${mnMessages:2}"))
	String getMoneySensorData() throws JsonProcessingException {
		int moneyPerStep = getMoneyPerStep();
		int coinsPerStep = moneyPerStep * getRandomNumber(0, 100) / 100;
		int cashPerStep = moneyPerStep - coinsPerStep;
		money[0] += coinsPerStep;
		money[1] += cashPerStep;
		SensorDTO sensor = new SensorDTO(machineId, nMoneySensor + MONEY_SENSOR_INDEX, money[nMoneySensor]);
		nMoneySensor++;
		if (nMoneySensor >= N_MONEY_SENSORS) {
			nMoneySensor = 0;
		}
		return mapper.writeValueAsString(sensor);
	}
	
	@InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "${cFixedDelay:3000}", maxMessagesPerPoll = "${cnMessages:10}"))
	String getCrashSensorData() throws JsonProcessingException {
		if(crashBoolean[nCrashSensor] == 0) {
			crashBoolean[nCrashSensor] = getRandomNumber(1, 100 / CRASH_PROBABILITY) == 1 ? 1 : 0;
		}
		SensorDTO sensor = new SensorDTO(machineId, nCrashSensor + CRASH_SENSOR_INDEX, crashBoolean[nCrashSensor]);
		nCrashSensor ++;
		if (nCrashSensor >= N_CRASH_SENSORS) {
			nCrashSensor = 0;
		}
		return mapper.writeValueAsString(sensor);
	}

	private int getMoneyPerStep() {
		int money = 0;
		for(int i = 0; i < productValue.length; i++)
			money += productValue[i];
		int res = money - currentMoney;
		currentMoney = money;
		return res;
	}

	private int getRandomNumber(int min, int max) {
		return random.ints(1, min, max + 1).findFirst().getAsInt();
	}
}
