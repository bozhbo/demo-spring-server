package com.snail.client.main;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ClientMain {

public static ConfigurableApplicationContext context = null;
	
	public static void start(String[] args) {
		SpringApplication springApplication = new SpringApplication(ClientMain.class);

		springApplication.setBannerMode(Mode.LOG);
		springApplication.setLogStartupInfo(true);

		context = springApplication.run(args);
	}

	public static void main(String[] args) {
		start(args);
	}
	
}
