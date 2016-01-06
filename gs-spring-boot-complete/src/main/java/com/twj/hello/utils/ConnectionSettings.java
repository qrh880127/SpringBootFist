package com.twj.hello.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 数据库连接信息
 * 
 * 	通过带有层次结构的properties获取数据库连接信息
 * 
 * 	@ConfigurationProperties(prefix="db.connection")
 * 	自动将前缀为"db.connection"的属性值注入到类的字段上
 * 
 * @author ruihua.qin
 *
 */
@Data
@Component
@ConfigurationProperties(prefix="db.connection")
public class ConnectionSettings {
	private String username;
	private String password;
	private String ip;
	private int port;
}
