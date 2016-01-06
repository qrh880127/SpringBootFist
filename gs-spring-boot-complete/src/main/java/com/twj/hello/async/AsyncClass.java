package com.twj.hello.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * 异步调用测试
 * 
 * @author ruihua.qin
 *
 */
@Component
public class AsyncClass {
    private final static Logger logger = LoggerFactory.getLogger(AsyncClass.class);

    /**
     * 异步方法
     * 
	 * 调用异步方法asyncClass.asyncMethod()
	 * asyncMethod方法由异步线程执行，本线程不等待其返回结果，
	 * 继续执行后续代码，
	 * 直到执行到代码ret.get()，本方法会阻塞，直至asyncMethod()方法执行完毕，返回结果后，方能继续执行代码
	 * 
     * @param say
     * @return
     */
	@Async
	public Future<Boolean> asyncMethod(String say) {
		logger.info("异步方法，这里执行耗时的异步逻辑 start");
		long start = System.currentTimeMillis();
		try {
			logger.info("Say: " + say);
			Thread.sleep(10*1000);
		} catch(Exception e) {
			
		}
		long end = System.currentTimeMillis();
		logger.info("异步方法，这里执行耗时的异步逻辑 end, cost:" + (end-start) + "ms");
		return new AsyncResult<Boolean>(true);
	}
	
	@Async
	public ListenableFuture<String> asyncMethod2(String say) {
		logger.info("异步方法ListenableFuture，这里执行耗时的异步逻辑 start");
		long start = System.currentTimeMillis();
		try {
			logger.info("Say: " + say);
			Thread.sleep(10*1000);
		} catch(Exception e) {
			
		}
		long end = System.currentTimeMillis();
		logger.info("异步方法ListenableFuture，这里执行耗时的异步逻辑 end, cost:" + (end-start) + "ms");
		return new AsyncResult<String>("ok");
	
		
	}
	
	/**
	 * CompletableFuture Java1.8
	 * 	这个太复杂了，留着找时间好好研究下...........
	 * @param say
	 * @return
	 */
	@Async
	public CompletableFuture<String> asyncMethod3(String say) {
		logger.info("异步方法CompletableFuture，这里执行耗时的异步逻辑 start");
		long start = System.currentTimeMillis();
		
		CompletableFuture<String> future = new CompletableFuture<>();
		try {
			logger.info("Say: " + say);
			Thread.sleep(10*1000);
		} catch(Exception e) {
			
		}
		long end = System.currentTimeMillis();
		logger.info("异步方法CompletableFuture，这里执行耗时的异步逻辑 end, cost:" + (end-start) + "ms");
		future.complete("ok");
		return future;
	}
}
