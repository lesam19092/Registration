package org.example.registration.service.auth;

import org.example.registration.model.dto.JwtRequest;
import org.example.registration.model.dto.PasswordResetRequest;
import org.example.registration.model.dto.RegistrationUserDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> createAuthToken(JwtRequest authRequest);

    ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto);

    ResponseEntity<?> resetPassword(PasswordResetRequest request);
}
