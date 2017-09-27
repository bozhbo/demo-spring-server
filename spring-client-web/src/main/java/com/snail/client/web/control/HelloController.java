package com.snail.client.web.control;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HelloController {
	
	@RequestMapping("/hello")
	public ModelAndView hello(Map<String, Object> model) {
		model.put("name", "[Angel -- 守护天使]");
		
		ModelAndView mv = new ModelAndView("hello_main");
		return mv;
	}
}
