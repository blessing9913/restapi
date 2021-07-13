package com.edmund.restapi.users.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
	private String userId;
	private String password;
}
