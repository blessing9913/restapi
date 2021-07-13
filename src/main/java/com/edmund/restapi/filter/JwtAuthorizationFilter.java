package com.edmund.restapi.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.edmund.restapi.config.JwtProperties;
import com.edmund.restapi.config.UserPrincipal;
import com.edmund.restapi.users.model.User;
import com.edmund.restapi.users.repository.UserRepository;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	private UserRepository userRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;

	}

	// 얻은 JWT가 제대로 된건지 확인
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		// Read the Authorization header in JWT
		String header = request.getHeader(JwtProperties.HEADER_STRING);

		// Check if header contain BEARER or is null
		if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			// rest of the spring pipeline
			chain.doFilter(request, response);
			return;
		}

		// If header is present, try grab user principal from database and perform
		// authorization
		Authentication authentication = getUsernamePasswordAuthentication(request);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Continue filter execution
		chain.doFilter(request, response);
	}

	/** Password Authentication **/
	private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
		// Get JWT token
		String token = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
		if (token != null) {
			// parse the token and validate it (decode)
			String userId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes())).build().verify(token).getSubject();

			// Search in the DB if we find the user by token subject (username)
			// If so, then grab user details and create spring auth token using username,
			// pass, authorities/roles
			if (userId != null) {
				//User user = (User) userRepository.findByUsername(username);
				User user = (User) userRepository.findByUserId(userId);
				UserPrincipal userPrincipal = new UserPrincipal(user);
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null, userPrincipal.getAuthorities());
				return auth;
			}
			return null;
		}
		return null;
	}

}