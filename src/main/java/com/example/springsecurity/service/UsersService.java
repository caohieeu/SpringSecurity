package com.example.springsecurity.service;

import com.example.springsecurity.dto.request.IntrospectRequest;
import com.example.springsecurity.dto.request.UpdateUserRequest;
import com.example.springsecurity.dto.response.UserResponse;
import com.example.springsecurity.exception.AppException;
import com.example.springsecurity.exception.ErrorCode;
import com.example.springsecurity.mapper.UserMapper;
import com.example.springsecurity.model.Users;
import com.example.springsecurity.repository.UsersRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.catalina.User;
import org.hibernate.sql.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UsersService {
    UsersRepository usersRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    AuthenticationService authenticationService;
    public List<Users> getListUser() {
        return usersRepository.findAll();
    }
    public Users getUser(String userId) {
        return usersRepository.findUserById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
    public UserResponse updateUser(String userId, UpdateUserRequest request) {
        Users user = getUser(userId);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(usersRepository.save(user));
    }
    public UserResponse deleteUser(String userId) {
        Users user = getUser(userId);

        if(user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        usersRepository.delete(user);
        return userMapper.toUserResponse(user);
    }

    public UserResponse getInfoUser(String token) throws ParseException, JOSEException {

        if(!authenticationService.checkToken(token)) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
        SignedJWT signedJWT = SignedJWT.parse(token);
        String userId = signedJWT.getJWTClaimsSet().getStringClaim("user_id");
        return userMapper.toUserResponse(
                getUser(userId)
        );
    }
}
