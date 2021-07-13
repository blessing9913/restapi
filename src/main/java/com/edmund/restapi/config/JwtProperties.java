package com.edmund.restapi.config;

public class JwtProperties {
	public static final String SECRET = "edmund"; // hash에 사용되는 key
	//public static final int EXPIRATION_TIME = 86400000; // token validation 기간(24시간 : 24 * 60 * 60 * 1000(밀리세컨드))
	//public static final int EXPIRATION_TIME = 24 * 60 * 60 * 1000; // token validation 기간(24시간 : 24 * 60 * 60 * 1000(밀리세컨드))
	public static final int EXPIRATION_TIME = 30 * 60 * 1000; // 30분
	public static final String TOKEN_PREFIX = "Bearer"; // header prefix
	public static final String HEADER_STRING = "Authorization"; // authorization header로 전달
}
