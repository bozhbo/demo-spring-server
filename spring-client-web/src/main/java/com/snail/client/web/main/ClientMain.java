package com.snail.client.web.main;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//import com.spring.logic.bean.GlobalBeanFactory;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.snail.client.web")
public class ClientMain {
	public static void start(String[] args) {
		SpringApplication springApplication = new SpringApplication(ClientMain.class);

		springApplication.setBannerMode(Mode.LOG);
		springApplication.setLogStartupInfo(true);
		
		ConfigurableApplicationContext context = springApplication.run(args);
		//GlobalBeanFactory.context = springApplication.run(args);
	}

	public static void main(String[] args) {
		start(args);
		
		
	}
}
