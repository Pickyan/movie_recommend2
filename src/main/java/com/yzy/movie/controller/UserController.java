package com.yzy.movie.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yzy.movie.entity.User;
import com.yzy.movie.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@RequestMapping("/login")
	public String login(String u_id,String u_passwd,HttpSession session) {
		User user = new User();
		user.setU_id(u_id);
		user.setU_passwd(u_passwd);
		
		int n = userService.login(user);
		
		if (n==0) {
			return "redirect:/login.jsp";
		}
		
		session.setAttribute("user", u_id);
		return "/movie/selectByName.jsp";
	}
}
