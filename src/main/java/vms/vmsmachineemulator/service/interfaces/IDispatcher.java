package vms.vmsmachineemulator.service.interfaces;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.MessageChannel;

public interface IDispatcher extends Sink {

  @Output("sensor")
  MessageChannel sendSensorData();
}