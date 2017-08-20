package com.spring.scene.main;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SceneServerMain {
	
	public static ConfigurableApplicationContext context = null;
	
	public static void start(String[] args) {
		SpringApplication springApplication = new SpringApplication(SceneServerMain.class);

		springApplication.setBannerMode(Mode.LOG);
		springApplication.setLogStartupInfo(true);

		context = springApplication.run(args);
	}

	public static void main(String[] args) {
		start(args);
	}
}
