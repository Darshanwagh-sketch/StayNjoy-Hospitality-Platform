package com.hospitality.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HospitalityPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalityPlatformApplication.class, args);
	}

}
