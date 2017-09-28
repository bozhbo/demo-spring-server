package com.snail.client.web.control;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.snail.client.web.control.form.LoginInfo;

@Controller
public class HelloController {
	
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	
	@RequestMapping("/login")
	public String login(@ModelAttribute LoginInfo loginInfo, Model model, HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();
		map.put("name", loginInfo.getAccount());
		
		model.addAttribute("role", map);
		
		messagingTemplate.convertAndSend("/topic/getResponse", loginInfo.getAccount());
		
		return "main_page";
	}
	
	@RequestMapping("/init")
	public String hello(Model model, HttpServletRequest request) {
		return "login";
	}
}
