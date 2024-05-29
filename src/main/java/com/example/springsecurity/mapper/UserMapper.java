package com.example.springsecurity.mapper;

import com.example.springsecurity.dto.request.RegisterRequest;
import com.example.springsecurity.dto.request.UpdateUserRequest;
import com.example.springsecurity.dto.response.UserResponse;
import com.example.springsecurity.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toUser(RegisterRequest request);
    UserResponse toUserResponse(Users user);
    void updateUser(@MappingTarget Users user, UpdateUserRequest request);
}
