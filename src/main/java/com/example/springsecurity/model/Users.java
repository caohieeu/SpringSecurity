package com.example.springsecurity.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "USERS")
public class Users {
    @Id
    @GeneratedValue
    Integer id;
    @Column(name = "USERNAME")
    String username;
    @Column(name = "FULLNAME")
    String fullname;
    @Column(name = "PASSWORD")
    String password;
}
