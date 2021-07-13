package com.edmund.restapi.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.edmund.restapi.config.JwtProperties;
import com.edmund.restapi.config.UserPrincipal;
import com.edmund.restapi.users.model.UserRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;

	/**
	 * authentication (Login request) POST /login
	 **/
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		UserRequestDto credentials = null;
		// 값 변환
		try {
			credentials = new ObjectMapper().readValue(request.getInputStream(), UserRequestDto.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Login Token, Spring Security가 이 사용자가 등록된 사용자인지 확인하기 위한 것
		//UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword(), new ArrayList<>());
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(credentials.getUserId(), credentials.getPassword(), new ArrayList<>());
		
		Authentication auth = authenticationManager.authenticate(authenticationToken);
		return auth;
	}

	/** 인증 성공시 수행 **/
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();

		// JWT 생성
		String token = JWT.create().withSubject(principal.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
		
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
	}
}
