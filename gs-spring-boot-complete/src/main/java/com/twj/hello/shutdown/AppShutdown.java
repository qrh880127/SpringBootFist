package com.twj.hello.shutdown;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

@Component
public class AppShutdown implements ExitCodeGenerator {
    private final static Logger logger = LoggerFactory.getLogger(AppShutdown.class);

    /*
     * 系统关闭前执行
     * 
     * @PreDestroy或者实现DisposableBean接口
     */
    @PreDestroy
	public void preDestroy() throws Exception {
		logger.info("系统关闭了");
	}

    /**
     * ExitCode
     */
	@Override
	public int getExitCode() {
		// TODO Auto-generated method stub
		return 0;
	}

}
