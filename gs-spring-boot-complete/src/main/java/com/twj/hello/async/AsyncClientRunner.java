package com.twj.hello.async;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

import lombok.extern.slf4j.Slf4j;

@Order(value=101)
@Component
@Slf4j
public class AsyncClientRunner implements CommandLineRunner{

    @Autowired
    private AsyncClass asyncClass;

	@Override
	public void run(String... args) throws Exception {
		log.info("------异步方法调用测试 start-------");
    	async1();
    	async2();
		log.info("------异步方法调用测试 end  -------");
	}
	
    
	/*
	 * Future
	 * 
	 * 调用异步方法asyncClass.asyncMethod()
	 * asyncMethod方法由异步线程执行，本线程不等待其返回结果，
	 * 继续执行后续代码，
	 * 直到执行到代码ret.get()，本方法会阻塞，直至asyncMethod()方法执行完毕，返回结果后，方能继续执行代码
	 */
	public void async1() throws Exception {
		log.info("定时任务 - 延迟1秒后首次调度，之后每5秒调用一次");
		//本句代码异步执行，不会等待返回结果
		Future<Boolean> ret = asyncClass.asyncMethod("hello world!");

		//以下代码会立即执行
		log.info("以下代码会立即执行");
		Thread.sleep(5*1000);
		
		
		//get()会阻塞，直到asyncMethod()执行完毕，并返回了结果
		Boolean retValue = ret.get();
		log.info("异步方法asyncClass.asyncMethod执行完毕，返回：" + retValue);
	}

	/*
	 * ListenableFuture<> 
	 * Spring的Future,增加回调函数功能
	 */
	public void async2() throws Exception {
		log.info("定时任务 - 延迟1秒后首次调度，之后每5秒调用一次");
		//本句代码异步执行，不会等待返回结果
		ListenableFuture<String> ret = asyncClass.asyncMethod2("hello world!");

		//回调函数
		SuccessCallback<String> successCallback = new SuccessCallback<String>() {
			@Override
			public void onSuccess(String result) {
				log.info("successCallback:" + result);
			}
		};
		FailureCallback failureCallback = new FailureCallback() {
			@Override
			public void onFailure(Throwable ex) {
				log.info("failureCallback");
			}
		};
		ret.addCallback(successCallback, failureCallback);
		//以下代码会立即执行
		log.info("以下代码会立即执行");
		Thread.sleep(5*1000);

		log.info("async2()方法允许结束");
	}
	
	/*
	 * CompletableFuture Java1.8
	 * 	这个太复杂了，留着找时间好好研究下...........
	 */
}
