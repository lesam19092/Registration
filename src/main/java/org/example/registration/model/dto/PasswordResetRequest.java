package org.example.registration.model.dto;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String name;
    private String newPassword;
    private String confirmPassword;
    private String confirmationCode;
}
