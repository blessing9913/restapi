package com.edmund.restapi.users.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.edmund.restapi.enums.Roles;
import com.edmund.restapi.users.model.User;
import com.edmund.restapi.users.model.UserResponseDto;
import com.edmund.restapi.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;

	public UserResponseDto getUser(String userId, Roles roles) {
		User user = this.userRepository.findByUserIdAndRole(userId, roles);
		
		UserResponseDto userResponseDto = new UserResponseDto(
			user.getUserId(), 
			user.getUserName(), 
			user.getPassword(), 
			user.getRole()
		);
		
		return userResponseDto;
	}

	public List<UserResponseDto> getUsers(Roles roles) {
		List<User> users = this.userRepository.findAllByRole(roles);
		
		return users.stream().map(user -> 
			new UserResponseDto(
				user.getUserId(), 
				user.getUserName(), 
				user.getPassword(), 
				user.getRole())
		).collect(Collectors.toList());
	}

	public List<UserResponseDto> getAdminUsers() {
		List<User> users = this.userRepository.findAll();
		
		return users.stream().map(user -> 
			new UserResponseDto(
				user.getUserId(), 
				user.getUserName(), 
				user.getPassword(), 
				user.getRole())
		).collect(Collectors.toList());
	}
}
