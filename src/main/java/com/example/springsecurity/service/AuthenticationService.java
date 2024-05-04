package com.example.springsecurity.service;

import com.example.springsecurity.dto.request.AuthenticateRequest;
import com.example.springsecurity.dto.request.RegisterRequest;
import com.example.springsecurity.exception.AppException;
import com.example.springsecurity.exception.ErrorCode;
import com.example.springsecurity.model.Users;
import com.example.springsecurity.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsersRepository usersRepository;
    public Users register(RegisterRequest request) {
        var user = usersRepository.findByUsername(request.getUsername())
                .orElse(null);
        if(user == null) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            return usersRepository.save(
                    Users.builder()
                            .fullname(request.getFullname())
                            .username(request.getUsername())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .build()
            );
        }
        throw new AppException(ErrorCode.USER_EXISTEED);
    }

    public boolean authenticate(AuthenticateRequest request) {
        var user = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }
}
