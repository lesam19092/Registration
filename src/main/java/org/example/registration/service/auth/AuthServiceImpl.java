package org.example.registration.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.registration.configuration.DurationConfig;
import org.example.registration.exception.DuplicateUsernameException;
import org.example.registration.exception.PasswordMismatchException;
import org.example.registration.model.dto.*;
import org.example.registration.model.entity.User;
import org.example.registration.service.jwt.JwtTokenService;
import org.example.registration.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final DurationConfig durationConfig;

    @Override
    public ResponseEntity<?> createAuthToken(JwtRequest authRequest) {
        log.info("Creating auth token for user: {}", authRequest.getUsername());

        String username = authRequest.getUsername();
        String password = authRequest.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        UserDetails userDetails = userService.loadUserByUsername(username);
        String token = jwtTokenService.generateToken(userDetails,
                authRequest.getRememberMe() ? durationConfig.getLongTime()
                        : durationConfig.getShortTime());

        log.info("Auth token created successfully for user: {}", username);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Override
    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {
        log.info("Creating new user: {}", registrationUserDto.getUsername());

        checkPassword(registrationUserDto);
        checkUsername(registrationUserDto);

        User user = userService.createNewUser(registrationUserDto);
        log.info("New user created successfully: {}", user.getUsername());
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getEmail()));
    }

    @Override
    public ResponseEntity<?> resetPassword(PasswordResetRequest request) {
        log.info("Resetting password for user: {}", request.getName());
        userService.resetPassword(request);
        log.info("Password reset successfully for user: {}", request.getName());
        return ResponseEntity.ok("Password reset successfully");
    }

    private void checkPassword(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            log.warn("Password mismatch for user: {}", registrationUserDto.getUsername());
            throw new PasswordMismatchException("Пароли не совпадают");
        }
    }

    private void checkUsername(RegistrationUserDto registrationUserDto) {
        if (userService.existsByUsername(registrationUserDto.getUsername())) {
            log.warn("Duplicate username found: {}", registrationUserDto.getUsername());
            throw new DuplicateUsernameException("Пользователь с таким именем уже существует");
        }
    }
}