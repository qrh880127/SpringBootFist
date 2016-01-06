package com.twj.hello.config;


import java.util.concurrent.CountDownLatch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

/**
 * 消息队列
 * 
 * 	Redis Messaging/PubSub, Redis消息、发布/订阅系统
 * 	
 * 	发布消息：stringRedisTemplate.convertAndSend("channel", "message"/Serializable);
 * 	接收消息：MsgReceiver.receiveMessage
 * 
 * 	可以注册多个监听器，或者启动多个客户端，每个客户端的每个监听器都能接收到消息
 * 
 * @author ruihua.qin
 *
 */
@Configuration
public class RedisMessageConfig {
	
	//同步锁
    @Bean  
    public CountDownLatch latch() {  
        return new CountDownLatch(1);  
    }

    //消息接收器
    @Bean  
    public MsgReceiver receiver(CountDownLatch latch) {  
        return new MsgReceiver(latch);  
    }

    //消息接收器
    @Bean  
    public MsgReceiver2 receiver2(CountDownLatch latch) {  
        return new MsgReceiver2(latch);  
    }

    /**
     * 消息监听器
     * 
     * 如果消息为对象，则需要设置JdkSerializationRedisSerializer序列化器，才能正确反序列化，
     * 默认使用的是StringRedisSerializer
     * 
     * @param receiver
     * @return
     */
    @Bean  
    public MessageListenerAdapter listenerAdapter(MsgReceiver receiver) {
    	//收到消息后，调用的方法
    	MessageListenerAdapter listenterAdapter = new MessageListenerAdapter(receiver, "receiveMessage");
    	
    	//如果消息为对象，则需要设置JdkSerializationRedisSerializer序列化器，才能正确反序列化，默认使用的是StringRedisSerializer
    	listenterAdapter.setSerializer(new JdkSerializationRedisSerializer());
        return listenterAdapter;
    }
    
    @Bean  
    public MessageListenerAdapter listenerAdapter2(MsgReceiver2 receiver) {
    	//收到消息后，调用的方法
    	MessageListenerAdapter listenterAdapter = new MessageListenerAdapter(receiver, "receiveMessage");
    	
    	//如果消息为对象，则需要设置JdkSerializationRedisSerializer序列化器，才能正确反序列化，默认使用的是StringRedisSerializer
    	listenterAdapter.setSerializer(new JdkSerializationRedisSerializer());
        return listenterAdapter;
    }

    /**
     * channel:	接收消息的channel
     * 
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean  
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,MessageListenerAdapter listenerAdapter) {  
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();  
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("channel"));  
        return container;  
    }  
    @Bean  
    public RedisMessageListenerContainer container2(RedisConnectionFactory connectionFactory,MessageListenerAdapter listenerAdapter2) {  
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();  
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter2, new PatternTopic("channel"));  
        return container;  
    }  
}
