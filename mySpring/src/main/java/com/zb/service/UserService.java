package com.zb.service;

import com.zb.spring.Autowired;
import com.zb.spring.Component;
import com.zb.spring.Scope;

@Component("userService")
@Scope("singleton")
public class UserService {
	
	@Autowired
	User user;
	
	public void test() {
		System.out.println(user);
	}
}
