package vms.vmsmachineemulator.configuration;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties("vms")
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode @ToString
public class EmulatorParams {
  private long sendInterval;
  private int maxValue;
  private int deltaMax;
  private int crashProbability;
  private Map<String, SensorProps> sensorRanges;
}