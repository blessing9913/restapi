package com.edmund.restapi.users.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edmund.restapi.annotation.Timer;
import com.edmund.restapi.enums.Roles;
import com.edmund.restapi.users.model.User;
import com.edmund.restapi.users.model.UserRequestDto;
import com.edmund.restapi.users.model.UserResponseDto;
import com.edmund.restapi.users.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("api")
public class UserController {
	private final UserService userService;

	// all
	@GetMapping("")
	public String home() {
		return "home";
	}

	// user
	@Timer
	@GetMapping("users/{userId}")
	public UserResponseDto user(@PathVariable String userId) {
		return this.userService.getUser(userId, Roles.USER);
	}
	
	// user
	@Timer
	@GetMapping("users")
	public List<UserResponseDto> users() {
		return this.userService.getUsers(Roles.USER);
	}

	// admin
	@Timer
	@GetMapping("admin/users")
	public List<UserResponseDto> adminUsers() {
		return this.userService.getAdminUsers();
	}
	
	@Timer
	@PostMapping("admin/test")
	public List<UserResponseDto> postTest(@RequestBody UserRequestDto userReq) {
		return this.userService.getAdminUsers();
	}
}
