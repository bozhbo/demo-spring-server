package com.snail.client.main.control;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientControl {

	@GetMapping("/templates")
    public String login(HttpServletRequest request){
		request.setAttribute("key", "hello world");
        return "login";
    }
	
	@GetMapping("/templates/login")
	public String commit(HttpServletRequest request){
		request.setAttribute("key", "hello world1");
        return "control";
    }
}
