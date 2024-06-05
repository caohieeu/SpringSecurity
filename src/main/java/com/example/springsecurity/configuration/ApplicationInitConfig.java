package com.example.springsecurity.configuration;

import com.example.springsecurity.enums.Role;
import com.example.springsecurity.model.Users;
import com.example.springsecurity.repository.UsersRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Configuration
public class ApplicationInitConfig {
    UsersRepository usersRepository;
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            if(usersRepository.findByUsername("admin").isEmpty()) {
                HashSet<String> roles = new HashSet<>();
                roles.add(Role.ADMIN.name());

                Users user = Users.builder()
                        .username("admin")
                        .fullname("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                usersRepository.save(user);
            }
        };
    }
}
