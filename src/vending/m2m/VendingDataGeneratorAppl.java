package vending.m2m;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class VendingDataGeneratorAppl {

		public static void main(String[] args) throws InterruptedException {
			ConfigurableApplicationContext ctx = SpringApplication.run(VendingDataGeneratorAppl.class, args);

		}

	}
