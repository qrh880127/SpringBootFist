package com.twj.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twj.hello.dao.PersonDao;

@Service
public class PersonService {
	@Autowired
	private PersonDao personDao;
	
	public void serviceDo() {
		personDao.dooo();
	}
}
