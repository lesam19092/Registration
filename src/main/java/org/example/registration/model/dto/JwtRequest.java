package org.example.registration.model.dto;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
    private Boolean rememberMe;
}