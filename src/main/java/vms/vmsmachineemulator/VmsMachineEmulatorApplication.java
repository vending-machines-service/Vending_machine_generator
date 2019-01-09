package vms.vmsmachineemulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import vms.vmsmachineemulator.service.interfaces.IEmulator;

@SpringBootApplication
public class VmsMachineEmulatorApplication {

	@Autowired
	static ApplicationContext ctx;
	
	public static void main(String[] args) {
		IEmulator emulator;
//		ApplicationContext ctx;
		ctx = SpringApplication.run(VmsMachineEmulatorApplication.class, args);
		emulator = ctx.getBean(IEmulator.class);
		emulator.startTest();
	}

}

