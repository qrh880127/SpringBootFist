package com.twj.hello.config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.twj.hello.user.domain.User;

public class MsgReceiver2 {
	//同步锁
    private CountDownLatch latch;
    
    @Autowired  
    public MsgReceiver2(CountDownLatch latch) {  
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
    public long toNanos(long duration) {
        throw new AbstractMethodError();
    }
    
    public static void main(String[] args) {
		long a = TimeUnit.SECONDS.toMillis(-1);
		System.out.println();
	}
}  
