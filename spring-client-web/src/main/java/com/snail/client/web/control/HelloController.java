package com.snail.client.web.control;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {
	
	@RequestMapping("/login")
	public String login(Model model, HttpServletRequest request) {
		return "login";
	}
	
	@RequestMapping("/hello")
	public String hello(Model model, HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();
		map.put("name", "bob");
		
		model.addAttribute("person", map);
		return "hello_main";
	}
}
