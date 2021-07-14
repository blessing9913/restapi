package com.edmund.restapi.aop;

import java.lang.reflect.Method;
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
import org.aspectj.lang.reflect.MethodSignature;
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

	@Pointcut("execution(* com.edmund.restapi.users.controller..*.*(..))")
	public void cut() {}
	
	@Before("cut()")
	public void before(JoinPoint joinPoint) {
		Gson gson = new Gson();
		
		// Request Method
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		logger.info("\n");
		logger.info("============ Request Method ============");
		logger.info("Request Method : {}", method);
		logger.info("============ Request Method ============");
		logger.info("\n");
		
		// Request Parameter
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		Map<String, String[]> paramMap = request.getParameterMap();
		if (paramMap.isEmpty() == false) {
			logger.info("\n");
			logger.info("============ Request Parameter ============");
			logger.info("Request Parameter : {}", paramMapToString(paramMap));
			logger.info("============ Request Parameter ============");
			logger.info("\n");
		}

		// Request Body
		Object[] args = joinPoint.getArgs();
		for(Object obj : args) {
			logger.info("\n");
			logger.info("============ Request Body ============");
			logger.info("Request Dto : {}", obj.getClass().getSimpleName());
			logger.info("Request Body : {}", gson.toJson(obj));
			logger.info("============ Request Body ============");
			logger.info("\n");
		}
	}
	
	@AfterReturning(value = "cut()", returning = "returnObj")
	public void afterReturn(JoinPoint joinPoint, Object returnObj) {
		Gson gson = new Gson();
		
		logger.info("\n");
		logger.info("============ Response ============");
		logger.info("Response Object : {}", gson.toJson(returnObj));
		logger.info("============ Response ============");
		logger.info("\n");
	}

	
//	@Around("com.edmund.restapi.aop.ParameterApp.onRequest()")
//	public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//
//		long start = System.currentTimeMillis();
//		try {
//			return pjp.proceed(pjp.getArgs());
//		} finally {
//			long end = System.currentTimeMillis();
//			logger.info("\n");
//			logger.info("============ Request Info ============");
//			logger.info("Result: {} {} < {} ({}ms)", request.getMethod(), request.getRequestURI(), request.getRemoteHost(), end - start);
//			logger.info("============ Request Info ============");
//			logger.info("\n");
//		}
//	}
}
