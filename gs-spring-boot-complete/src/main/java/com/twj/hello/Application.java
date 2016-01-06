package com.twj.hello;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


/*
 * @SpringBootApplication
 * 很多Spring Boot开发者总是使用@Configuration，@EnableAutoConfiguration和@ComponentScan注解他们的main类。
 * 因此Spring提供了@SpringBootApplication注解等价于以默认属性使用@Configuration，@EnableAutoConfiguration和@ComponentScan
 *
 *
 * @Configuration
 *
 * 
 * @EnableAutoConfiguration
 * 尝试根据你添加的jar依赖自动配置你的Spring应用.
 * 注：你只需要添加一个@EnableAutoConfiguration注解。我们建议你将它添加到主@Configuration类上
 * 如你不想要的特定自动配置类，你可以使用@EnableAutoConfiguration注解的排除属性来禁用它们
 * 		如:@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
 * 
 * 
 * @ComponentScan
 * 你的所有应用程序组件（@Component, @Service, @Repository, @Controller等）将被自动注册为Spring Beans
 */

//@Configuration
//@EnableAutoConfiguration
//@ComponentScan
@SpringBootApplication	// same as @Configuration @EnableAutoConfiguration @ComponentScan
@EnableScheduling		//任务调度
@EnableAsync			//允许异步任务调度
@EnableBatchProcessing	//批处理
public class Application {
    
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        
        System.out.println("Let's inspect the beans provided by Spring Boot:");
        
        /*String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }*/
        
        //SpringApplication.exit(ctx);
    }

}
