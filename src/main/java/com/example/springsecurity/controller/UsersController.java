package com.example.springsecurity.controller;

import com.example.springsecurity.dto.request.RegisterRequest;
import com.example.springsecurity.dto.request.UpdateUserRequest;
import com.example.springsecurity.dto.response.ApiResponse;
import com.example.springsecurity.dto.response.UserResponse;
import com.example.springsecurity.exception.ErrorCode;
import com.example.springsecurity.exception.SuccessCode;
import com.example.springsecurity.model.Users;
import com.example.springsecurity.repository.UsersRepository;
import com.example.springsecurity.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UsersController {
    private final UsersService usersService;
    private final UsersRepository usersRepository;
    @GetMapping("/")
    public ApiResponse<?> getUsers() {
        return ApiResponse.builder()
                .code(1000)
                .result(usersService.getListUser())
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
                .code(1000)
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
}
