package com.edmund.restapi.users.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.edmund.restapi.enums.Roles;
import com.edmund.restapi.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String userId;
	
	@Column(nullable = false)
	private String userName;
	
	@Column(nullable = false)
	private String password;
	
	
	private Status status;
	
	
	private Roles role;
	
	@Builder
	public User(String userId, String userName, String password, Roles role) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.role = role;
		this.status = Status.NONBLOCKED;
	}
}
