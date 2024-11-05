package com.laundering.laundering_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 스케줄러 활성화
public class LauneringServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LauneringServerApplication.class, args);
	}

}
