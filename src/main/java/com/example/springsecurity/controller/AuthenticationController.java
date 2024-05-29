package com.example.springsecurity.controller;

import com.example.springsecurity.dto.request.AuthenticateRequest;
import com.example.springsecurity.dto.request.IntrospectRequest;
import com.example.springsecurity.dto.request.RegisterRequest;
import com.example.springsecurity.dto.response.ApiResponse;
import com.example.springsecurity.dto.response.AuthenticateResponse;
import com.example.springsecurity.dto.response.IntrospectResponse;
import com.example.springsecurity.model.Users;
import com.example.springsecurity.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ApiResponse<Users> register(
            @RequestBody @Valid RegisterRequest user
    ) {
        var userRegister = authenticationService.register(user);

        return ApiResponse.<Users>builder()
                .result(userRegister)
                .build();
    }

    @PostMapping("/log-in")
    public ApiResponse<AuthenticateResponse> login(
            @RequestBody AuthenticateRequest user
    ) throws JOSEException {
        var userRegister = authenticationService.authenticate(user);

        return ApiResponse.<AuthenticateResponse>builder()
                .result(userRegister)
                .build();
    }
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(
            @RequestBody IntrospectRequest introspectRequest
    ) throws JOSEException, ParseException {
        var introspected = authenticationService.introspectToken(introspectRequest);

        return ApiResponse.<IntrospectResponse>builder()
                .result(introspected)
                .build();
    }
}
