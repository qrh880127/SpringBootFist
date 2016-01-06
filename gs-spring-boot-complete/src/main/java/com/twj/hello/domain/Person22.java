package com.twj.hello.domain;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Person22 {
	/*
	 * @Value("${name}")
	 * 将配置的name的值注入到bean里,本例取自application。properties
	 */
	
	@Value("${name}")
	private String name;
	
	@Value("${rand.string}")
	private String randomStr;
	
	@Value("${rand.int}")
	private int randomInt;

	@Value("${rand.bignumber}")
	private Long randomLong;

	@Value("${rand.intRange}")
	private int intRange;

	@Value("${rand.lesThan10}")
	private int lesThan10;
	
	private int age;
}
