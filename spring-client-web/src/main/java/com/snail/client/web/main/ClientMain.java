package com.snail.client.web.main;

import org.directwebremoting.servlet.DwrServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import com.snail.client.web.control.ClientControl;
import com.snail.client.web.robot.GFRobot;
import com.spring.logic.util.LogicUtil;

//import com.spring.logic.bean.GlobalBeanFactory;

@Configuration
@EnableAutoConfiguration
@EnableAsync
@ComponentScan("com.snail")
public class ClientMain {
	public static void main(String[] args) {
		SpringApplication.run(ClientMain.class, args);
		
		LogicUtil.initJson();
		
		ClientControl.init();
		
		GFRobot robot = new GFRobot();
		robot.start();
	}
	
	@Bean  
	public ServletRegistrationBean filterRegistrationBean(){
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(); 
		
		DwrServlet dwrServlet = new DwrServlet();
		servletRegistrationBean.setServlet(dwrServlet);
		servletRegistrationBean.setEnabled(true);  
		servletRegistrationBean.addUrlMappings("/dwr/*");
		servletRegistrationBean.setLoadOnStartup(1);
		
	    return servletRegistrationBean;
	}
}


