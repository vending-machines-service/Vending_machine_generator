package vending.m2m;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class VendingDataGeneratorAppl {
		private static final long TIME_OUT = 5000;

		public static void main(String[] args) throws InterruptedException {
			ConfigurableApplicationContext ctx = SpringApplication.run(VendingDataGeneratorAppl.class, args);
			Thread.sleep(TIME_OUT);
			ctx.close();
		}

	}
