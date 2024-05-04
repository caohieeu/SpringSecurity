package com.example.springsecurity.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticateResponse {
    private boolean authenticated;
}
