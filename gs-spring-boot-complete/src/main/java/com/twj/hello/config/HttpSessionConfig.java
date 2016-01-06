package com.twj.hello.config;

import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * 允许SpringSession
 * 
 * 注册一个springSessionRepositoryFilter,它会（透明的）自动替换HttpSession,并通过Redis实现分布式Session
 * 
 * 你只需要像原来的方式一样使用HtttpSession即可
 * 
 * 使用SpringSession需要做的事
 * 	1.依赖中引入spring-session，和Redis
 *			<dependency>
                <groupId>org.springframework.session</groupId>
                <artifactId>spring-session</artifactId>
        	</dependency>
 *	
 *	2. 添加注解@EnableRedisHttpSession
 *
 *	其实，只引入spring-session即可，它会自动添加@EnableRedisHttpSession
 *	但为了可读性更强，最好显示增加注解配置
 *
 *
 * @author ruihua.qin
 *
 */
@EnableRedisHttpSession
public class HttpSessionConfig {

	/**
	 * Session头策略  - Spring Session RESTful APIs 
	 * 		第一次请求后，会在请求响应头增加x-auth-token字段，如下：
	 * 		x-auth-token:cefb3b3b-edd8-41b8-9ee8-2d8142d0a1be
	 * 
	 *  该值为Session，以后的请求*必须*直接使用它，把它放到请求头里，来通过集群验证,
	 *  因为浏览器不会主动把它放入Cookies里，也不会主动把它放到之后的请求头里
	 */
	//@Bean
    public HttpSessionStrategy httpSessionStrategy() {
            return new HeaderHttpSessionStrategy(); 
    }
}
