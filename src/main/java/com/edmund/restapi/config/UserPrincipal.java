package com.edmund.restapi.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.edmund.restapi.enums.Status;
import com.edmund.restapi.users.model.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
	private static final long serialVersionUID = -6382741982373349076L;
	private final User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + this.user.getRole().name());
		authorities.add(authority);
		
		return authorities;
	}

	@Override
	public String getPassword() {
		 return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUserId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		if (this.user.getStatus() == Status.BLOCKED) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		if (this.user.getStatus() == Status.BLOCKED) {
			return false;
		}
		return true;
	}

}
