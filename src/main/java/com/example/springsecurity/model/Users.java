package com.example.springsecurity.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "USERS")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(name = "USERNAME")
    String username;
    @Column(name = "FULLNAME")
    String fullname;
    @Column(name = "PASSWORD")
    String password;
    Set<String> roles;
}
