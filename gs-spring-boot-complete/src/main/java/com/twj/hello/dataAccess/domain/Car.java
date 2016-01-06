package com.twj.hello.dataAccess.domain;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor	//为所有final和非空（译注：带有@NonNull注解)字段生成一个构造方法 
public class Car {
	@Id
	private String id;
	
	@NonNull
	private String name;
	
	@NonNull
	private String factory;
	
	private CarProduceInfo carProduceInfo;	//生产信息
}
