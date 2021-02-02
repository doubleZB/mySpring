package com.zb.demo;

import com.zb.service.UserService;
import com.zb.spring.ZBAnnotationConfigApplicationContext;

public class Test {

	public static void main(String[] args) {
	
		//Æô¶¯spring
		ZBAnnotationConfigApplicationContext context = new ZBAnnotationConfigApplicationContext(AppConfig.class);
		UserService userService =(UserService) context.getBean("userService");
		System.out.println(userService);
		userService.test();
		
	}
}
