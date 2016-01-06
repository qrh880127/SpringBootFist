package com.twj.hello.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.twj.hello.domain.Quote;

/**
 * StartupRunner实现了CommandLineRunner接口
 * 这个方法会在应用程序启动后首先被调用
 * 
 * @author ruihua.qin
 *
 */
@Component
@Order(value=1)
public class StartupRunner implements CommandLineRunner{
    private final static Logger logger = LoggerFactory.getLogger(StartupRunner.class);

	@Override
	public void run(String... args) throws Exception {
		logger.info("实现了CommandLineRunner接口,这个方法会在应用程序启动后首先被调用");
		
		//Http Get/Post 请求Json并转为对象
		RestTemplate restTemplate = new RestTemplate();
        Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
        //quote = restTemplate.postForObject("http://gturnquist-quoters.cfapps.io/api/random", null, Quote.class);
        logger.info(quote.toString());
        
	}

}
