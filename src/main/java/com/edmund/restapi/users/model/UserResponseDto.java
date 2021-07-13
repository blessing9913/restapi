package com.edmund.restapi.users.model;

import com.edmund.restapi.enums.Roles;
import com.edmund.restapi.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
	
	private String userId;
	
	private String userName;
	
	private Status status;
	
	private Roles role;
	
	@Builder
	public UserResponseDto(String userId, String userName, String password, Roles role) {
		this.userId = userId;
		this.userName = userName;
		this.role = role;
		this.status = Status.NONBLOCKED;
	}
}
