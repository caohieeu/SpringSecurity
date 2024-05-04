package com.example.springsecurity.controller;

import com.example.springsecurity.dto.response.ApiResponse;
import com.example.springsecurity.model.Users;
import com.example.springsecurity.repository.UsersRepository;
import com.example.springsecurity.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UsersController {
    private final UsersService usersService;
    @GetMapping("/")
    public ApiResponse<?> getUsers() {
        return ApiResponse.builder()
                .code(1000)
                .result(usersService.getListUser())
                .build();
    }
}
