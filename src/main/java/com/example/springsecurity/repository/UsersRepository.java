package com.example.springsecurity.repository;

import com.example.springsecurity.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  UsersRepository extends JpaRepository<Users, Integer> {
    Boolean existsByUsername(String username);
    Optional<Users> findByUsername(String username);
}
