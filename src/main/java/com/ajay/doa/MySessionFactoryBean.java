package com.ajay.doa;

import java.io.IOException;

import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

public class MySessionFactoryBean extends LocalSessionFactoryBean {
	public void init() {
		try {
			afterPropertiesSet();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Method called");
	}
}
