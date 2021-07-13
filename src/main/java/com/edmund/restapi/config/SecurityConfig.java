package com.edmund.restapi.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.edmund.restapi.enums.Roles;
import com.edmund.restapi.filter.JwtAuthenticationFilter;
import com.edmund.restapi.filter.JwtAuthorizationFilter;
import com.edmund.restapi.users.repository.UserRepository;
import com.edmund.restapi.users.service.UserPrincipalDetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserPrincipalDetailsService userPrincipalDetailsService;
	private final UserRepository userRepository;
	
	@Bean // Security에서 제공하는 비밀번호 암호화 객체
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// static file 무시
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
		web.ignoring().antMatchers("/h2-console/**");
	}

	//http 관련 인증
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//super.configure(http);
		
		http
			.csrf().disable() // JWT에서는 필요 없음
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// rest: stateless, cookie에 세션 저장 x
			.and()
				.authorizeRequests()
				.antMatchers("/api/", "/login").permitAll()
				.antMatchers("/api/users/**").hasAnyRole(Roles.USER.name(), Roles.ADMIN.name())
				.antMatchers("/api/admin/**").hasRole(Roles.ADMIN.name())
				.antMatchers("/h2-console/**").permitAll() // 누구나 h2-console 접속 허용
			.and()
				.addFilter(new JwtAuthenticationFilter(authenticationManager()))
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), this.userRepository));
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider());
	}
	
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		// db의 비밀번호 암호화/복호화
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);
		return daoAuthenticationProvider;
	}
}
