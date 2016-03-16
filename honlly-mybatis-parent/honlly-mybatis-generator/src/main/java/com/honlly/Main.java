package com.honlly;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		try {
			@SuppressWarnings("resource")
			ApplicationContext context = new ClassPathXmlApplicationContext("spring/*-spring.xml");
			EntityCreater entityCreater = context.getBean(EntityCreater.class);
			entityCreater.create();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
