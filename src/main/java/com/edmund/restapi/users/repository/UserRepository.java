package com.edmund.restapi.users.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edmund.restapi.enums.Roles;
import com.edmund.restapi.users.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUserName(String userName);
	User findByUserId(String userId);
	User findByUserIdAndRole(String userId, Roles role);
	List<User> findAllByRole(Roles role);
}
