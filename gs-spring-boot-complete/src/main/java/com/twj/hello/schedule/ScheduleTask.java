package com.twj.hello.schedule;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

import com.twj.hello.async.AsyncClass;

//@Component
public class ScheduleTask {
    private final static Logger logger = LoggerFactory.getLogger(ScheduleTask.class);
    
    @Autowired
    private AsyncClass asyncClass;
	

	/*
	 * @Scheduled
	 *	任务调度
	 * 	@Scheduled(fixedRate=5000)		每5秒调用一次
	 * 	@Scheduled(fixedDelay=5000)		每次调用完成后5秒执行下一次调度
	 * 	@Scheduled(initialDelay=1000, fixedRate=5000)	延迟1秒后首次调度，之后每5秒调用一次
	 * 	@Scheduled(initialDelay=1000, fixedDelay=5000)	延迟1秒后首次调度，之后每次调用完成后5秒执行下一次调度
	 * 	@Scheduled(cron="* * * * * *")	Cron表达式进行调度
	 * 
	 * 	参考：http://docs.spring.io/spring/docs/current/spring-framework-reference/html/scheduling.html#scheduling-annotation-support-scheduled
	 */
	@Scheduled(fixedRate=5000)
	public void job1() {
		logger.info("定时任务 - 每5秒执行一次");
	}
	

	/*
	 * 调用异步方法asyncClass.asyncMethod()
	 * asyncMethod方法由异步线程执行，本线程不等待其返回结果，
	 * 继续执行后续代码，
	 * 直到执行到代码ret.get()，本方法会阻塞，直至asyncMethod()方法执行完毕，返回结果后，方能继续执行代码
	 */
	@Scheduled(initialDelay=1000, fixedDelay=5000)
	public void job2() throws Exception {
		logger.info("定时任务 - 延迟1秒后首次调度，之后每5秒调用一次");
		//本句代码异步执行，不会等待返回结果
		Future<Boolean> ret = asyncClass.asyncMethod("hello world!");

		//以下代码会立即执行
		logger.info("以下代码会立即执行");
		Thread.sleep(5*1000);
		
		
		//get()会阻塞，直到asyncMethod()执行完毕，并返回了结果
		Boolean retValue = ret.get();
		logger.info("异步方法asyncClass.asyncMethod执行完毕，返回：" + retValue);
	}

	@Scheduled(initialDelay=1000, fixedDelay=5000)
	public void job3() throws Exception {
		logger.info("定时任务 - 延迟1秒后首次调度，之后每5秒调用一次");
		//本句代码异步执行，不会等待返回结果
		ListenableFuture<String> ret = asyncClass.asyncMethod2("hello world!");

		//回调函数
		SuccessCallback<String> successCallback = new SuccessCallback<String>() {
			@Override
			public void onSuccess(String result) {
				logger.info("successCallback:" + result);
			}
		};
		FailureCallback failureCallback = new FailureCallback() {
			@Override
			public void onFailure(Throwable ex) {
				logger.info("failureCallback");
			}
		};
		ret.addCallback(successCallback, failureCallback);
		//以下代码会立即执行
		logger.info("以下代码会立即执行");
		Thread.sleep(5*1000);
		

		String retValue = ret.get();  //会阻塞，直到asyncMethod()执行完毕，并返回了结果
		logger.info("异步方法asyncClass.asyncMethod执行完毕，返回：" + retValue);
	}
}
