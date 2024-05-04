package com.example.springsecurity.service;

import com.example.springsecurity.model.Users;
import com.example.springsecurity.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    public List<Users> getListUser() {
        return usersRepository.findAll();
    }
}
