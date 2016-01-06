package com.twj.hello.user.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String username;
	private String password;
}
