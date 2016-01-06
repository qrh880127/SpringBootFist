package com.twj.hello.batch;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 		lombok详细使用说明
 
 
        @Data   ：注解在类上；提供类所有属性的 getting 和 setting 方法，此外还提供了equals、canEqual、hashCode、toString 方法
        @AllArgsConstructor：注解在类上；为类提供一个全参的构造方法
        @Slf4j	：注解在类上；为类提供一个 属性名为log 的 slf4j 日志对象
        @Log4j 	：注解在类上；为类提供一个 属性名为log 的 log4j 日志对象
        
        @Setter：注解在属性上；为属性提供 setting 方法
        @Getter：注解在属性上；为属性提供 getting 方法
        @NoArgsConstructor：注解在类上；为类提供一个无参的构造方法
 * 
 * 
 * 
 * 
 * @author ruihua.qin
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
	private String firstName;
	private String lastName;
	private int		age;
}
