package vms.vmsmachineemulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@SpringBootApplication
@ManagedResource
public class VmsMachineEmulatorApplication {
	private static ConfigurableApplicationContext ctx;
	public static void main(String[] args) {
		ctx = SpringApplication.run(VmsMachineEmulatorApplication.class, args);
		ctx.stop();
	}

	@ManagedOperation
	public static void stop() {
		ctx.stop();
	}
}

