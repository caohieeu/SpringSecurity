package com.example.springsecurity.controller;

import com.example.springsecurity.dto.request.IntrospectRequest;
import com.example.springsecurity.dto.request.RegisterRequest;
import com.example.springsecurity.dto.request.UpdateUserRequest;
import com.example.springsecurity.dto.response.ApiResponse;
import com.example.springsecurity.dto.response.UserResponse;
import com.example.springsecurity.exception.ErrorCode;
import com.example.springsecurity.exception.SuccessCode;
import com.example.springsecurity.mapper.UserMapper;
import com.example.springsecurity.model.Users;
import com.example.springsecurity.repository.UsersRepository;
import com.example.springsecurity.service.UsersService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/user")
public class UsersController {
    UsersService usersService;
    UsersRepository usersRepository;
    UserMapper userMapper;
    @GetMapping
    public ApiResponse<?> getUsers() {
        return ApiResponse.builder()
                .result(usersService.getListUser())
                .build();
    }
    @GetMapping("/{user_id}")
    public ApiResponse<UserResponse> getUser(
            @PathVariable("user_id") String user_id
    ) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Username: " + authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> System.out.println(grantedAuthority.getAuthority()));
        UserResponse userResponse = userMapper.toUserResponse(usersService.getUser(user_id));

        return ApiResponse.<UserResponse>builder()
                .result(userResponse)
                .build();
    }
    @PostMapping("/add")
    public ApiResponse<Users> addUser() {
        return null;
    }

    @PutMapping("/update/{userId}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable("userId") String userId,
            @RequestBody UpdateUserRequest request
    ) {
        return ApiResponse.<UserResponse>builder()
                .result(usersService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(
            @PathVariable("userId") String userId
    ) {
        UserResponse userDeleted = usersService.deleteUser(userId);

        if (userDeleted != null) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body("Error Delete");
    }
    @GetMapping("/my-info/{token}")
    public ApiResponse<UserResponse> infoUser(
            @PathVariable String token
    ) throws ParseException, JOSEException {
        return ApiResponse.<UserResponse>builder()
                .result(usersService.getInfoUser(token))
                .build();
    }
}
