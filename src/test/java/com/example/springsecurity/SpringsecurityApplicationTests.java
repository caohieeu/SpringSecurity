package com.example.springsecurity;

import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class SpringsecurityApplicationTests {

//	@Test
//	void contextLoads() {
//
//	}

	@Test
	void hash() throws NoSuchAlgorithmException {
		String password = "12312312321312";
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());

		byte[] digest = md.digest();
		String md5Hash = DatatypeConverter.printHexBinary(digest);

		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

		System.out.println("MD5 round 1: " + md5Hash);
		System.out.println("BCrypt round 1: " + passwordEncoder.encode(password));
	}
}
