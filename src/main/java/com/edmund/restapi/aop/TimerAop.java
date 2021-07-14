package com.edmund.restapi.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class TimerAop {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Pointcut("execution(* com.edmund.restapi.users.controller..*.*(..))")
	private void cut() {}
	
	
	@Pointcut("@annotation(com.edmund.restapi.annotation.Timer)")
	private void enableTimer() {}
	
	@Around("cut() && enableTimer()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		
		stopWatch.start();
		
		Object result = joinPoint.proceed(joinPoint.getArgs());
		
		stopWatch.stop();
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		logger.info("\n");
		logger.info("============ Response Time ============");
		logger.info("Total Time : {} {} {} -> {}ms", request.getMethod(), request.getRequestURI(), request.getRemoteHost(), stopWatch.getTotalTimeMillis());
		logger.info("============ Response Time ============");
		logger.info("\n");
		
		return result;
	}
}
