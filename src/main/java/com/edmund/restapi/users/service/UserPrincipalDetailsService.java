package com.edmund.restapi.users.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.edmund.restapi.config.UserPrincipal;
import com.edmund.restapi.users.model.User;
import com.edmund.restapi.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserPrincipalDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	// find user by its userId
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		// userId와 일치하는 사용자를 찾음
		User user = this.userRepository.findByUserId(userId);

		// db에서 찾은 사용자를 사용해 UserPrincipal 객체 생성
		UserPrincipal userPrincipal = new UserPrincipal(user);

		// userDetail을 구현한 userPrincipal을 리턴
		return userPrincipal;
	}
}
