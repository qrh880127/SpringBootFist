package com.twj.hello.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twj.hello.service.PersonService;

@RestController	//字符串的形式渲染结果
public class HelloController {
	//定义一个全局的记录器，通过LoggerFactory获取  
    private final static Logger logger = LoggerFactory.getLogger(HelloController.class);   
    
    /*
     * @Autowired
     * 自动装配，无需写Getter/Setter方法
     */
    @Autowired
    private PersonService personService;
    
    @RequestMapping("/a")
    public String index() {
    	logger.info("Logback info~的的");
    	
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/b")
    public String b() {
    	
    	logger.debug("logger debug");
    	logger.info("logger info");
    	logger.error("logger error");
    	personService.serviceDo();
        return "Greetings from 	 Boot!ccc:";
    }
}
