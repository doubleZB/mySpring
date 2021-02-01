package com.zb.service;

import com.zb.spring.Autowired;
import com.zb.spring.Component;

@Component("userService")
public class UserService {
	
	@Autowired
	User user;
	
	public void test() {
		System.out.println(user);
	}
}
