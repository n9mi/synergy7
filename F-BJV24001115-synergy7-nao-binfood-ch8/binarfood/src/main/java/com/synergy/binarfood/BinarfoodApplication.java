package com.synergy.binarfood;

import com.synergy.binarfood.setup.Setup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class BinarfoodApplication {

	public static void main(String[] args) {
		Setup setup = SpringApplication.run(BinarfoodApplication.class)
				.getBean(Setup.class);
		setup.setup();
	}

}
