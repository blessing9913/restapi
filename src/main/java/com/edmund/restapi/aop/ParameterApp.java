package com.edmund.restapi.aop;

import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.base.Joiner;
import com.google.gson.Gson;

@Aspect
@Component
public class ParameterApp {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String paramMapToString(Map<String, String[]> paramMap) {
		return paramMap.entrySet().stream()
				.map(entry -> String.format("%s -> (%s)", entry.getKey(), Joiner.on(",").join(entry.getValue())))
				.collect(Collectors.joining(", "));
	}

	@Pointcut("within(com.edmund.restapi.users.controller..*)")
	public void onRequest() {
	}
	
	@Before("onRequest()")
	public void before(JoinPoint joinPoint) {
		Gson gson = new Gson();
		
		// Request Parameter
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		Map<String, String[]> paramMap = request.getParameterMap();
		if (paramMap.isEmpty() == false) {
			logger.debug("============ Request Parameter ============");
			logger.debug("Request Parameter : {}", paramMapToString(paramMap));
			logger.debug("============ Request Parameter ============");
		}

		// Request Body
		Object[] args = joinPoint.getArgs();
		for(Object obj : args) {
			logger.debug("============ Request Body ============");
			logger.debug("Request Model Class : {}", obj.getClass().getSimpleName());
			logger.debug("Request Body : {}", gson.toJson(obj));
			logger.debug("============ Request Body ============");
		}
	}
	
	@AfterReturning(value = "onRequest()", returning = "returnObj")
	public void afterReturn(JoinPoint joinPoint, Object returnObj) {
		Gson gson = new Gson();
		
		logger.debug("============ Response ============");
		logger.debug("Response Object : {}", gson.toJson(returnObj));
		logger.debug("============ Response ============");
	}

	@Around("com.edmund.restapi.aop.ParameterApp.onRequest()")
	public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

		long start = System.currentTimeMillis();
		try {
			return pjp.proceed(pjp.getArgs());
		} finally {
			long end = System.currentTimeMillis();
			
			logger.debug("============ Request Info ============");
			logger.debug("Result: {} {} < {} ({}ms)", request.getMethod(), request.getRequestURI(), request.getRemoteHost(), end - start);
			logger.debug("============ Request Info ============");
		}
	}
}
