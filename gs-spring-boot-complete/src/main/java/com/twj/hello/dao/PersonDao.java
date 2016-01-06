package com.twj.hello.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.twj.hello.domain.Person22;
import com.twj.hello.utils.ConnectionSettings;

@Component
public class PersonDao {
	@Autowired
	private Person22 person;
	
	@Autowired
	private ConnectionSettings connectionSettings;
	
	public void dooo() {
		System.out.println("PersonDao dooo()");
		//Person person = new Person();
		System.out.println(person.getName());
		System.out.println(person.getRandomStr());
		System.out.println(person.getRandomInt());
		System.out.println(person.getRandomLong());
		System.out.println(person.getIntRange());
		System.out.println(person.getLesThan10());
		
		
		System.out.println(connectionSettings.getUsername() + "/" + connectionSettings.getPassword() + "/" + connectionSettings.getIp() + "/" + connectionSettings.getPort());
	}
}
