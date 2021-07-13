package com.edmund.restapi.db.init;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edmund.restapi.enums.Roles;
import com.edmund.restapi.users.model.User;
import com.edmund.restapi.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class DBInit implements CommandLineRunner {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * 매번 회원가입 하지 않고 어플리케이션 실행 되면 사용자 정보를 미리 넣음(테스트 용도)
	 */
	@Override
	public void run(String... args) throws Exception {
		// initialize
		this.userRepository.deleteAll();

		// user, admin+password encode
		User user = new User("user01", "User01", passwordEncoder.encode("1234"), Roles.USER);
		User admin = new User("admin", "Admin", passwordEncoder.encode("1234"), Roles.ADMIN);

		List<User> users = Arrays.asList(user, admin);
		this.userRepository.saveAll(users);
	}
}
