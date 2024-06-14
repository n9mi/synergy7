package com.synergy.binarfood.core;

import com.synergy.binarfood.core.setup.Setup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BinarfoodCoreApplication {

	public static void main(String[] args) {
		Setup setup = SpringApplication.run(BinarfoodCoreApplication.class)
				.getBean(Setup.class);
		setup.setup();
	}

}
