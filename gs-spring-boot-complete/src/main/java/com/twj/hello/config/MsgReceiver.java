package com.twj.hello.config;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;

import com.twj.hello.user.domain.User;

public class MsgReceiver {
	//同步锁
    private CountDownLatch latch;
    
    @Autowired  
    public MsgReceiver(CountDownLatch latch) {  
        this.latch = latch;  
    }
    
    
    /**
     * 收到消息
     * 
     * @param message
     */
    public void receiveMessage(User message, String channel) {  
        System.out.println("Received <" + message + ">");  
        latch.countDown();  
    }  
    public void receiveMessage(String message, String channel) {  
        System.out.println("Received <" + message + ">");  
        latch.countDown();  
    } 
}  
