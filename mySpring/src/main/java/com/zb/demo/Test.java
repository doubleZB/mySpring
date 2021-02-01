package com.zb.demo;

import com.zb.spring.ZBAnnotationConfigApplicationContext;

public class Test {

	public static void main(String[] args) {
	
		//Æô¶¯spring
		ZBAnnotationConfigApplicationContext context = new ZBAnnotationConfigApplicationContext(AppConfig.class);
		Object userService = context.getBean("userService");
		System.out.println(userService);
		
	}
}
