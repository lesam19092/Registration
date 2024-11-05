package org.example.registration.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.registration.exception.DuplicateUsernameException;
import org.example.registration.exception.PasswordMismatchException;
import org.example.registration.model.dto.JwtRequest;
import org.example.registration.model.dto.JwtResponse;
import org.example.registration.model.dto.RegistrationUserDto;
import org.example.registration.model.dto.UserDto;
import org.example.registration.model.entity.User;
import org.example.registration.service.jwt.JwtTokenService;
import org.example.registration.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<?> createAuthToken(JwtRequest authRequest) {

        String username = authRequest.getUsername();
        String password = authRequest.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = userService.loadUserByUsername(username);
        String token = jwtTokenService.generateToken(userDetails, authRequest.getRememberMe() ? 30 * 24 * 60 : 10);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Override
    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {

        checkPassword(registrationUserDto);
        checkUsername(registrationUserDto);

        User user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getEmail()));
    }


    private void checkPassword(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            throw new PasswordMismatchException("Пароли не совпадают");
        }
    }

    private void checkUsername(RegistrationUserDto registrationUserDto) {
        if (userService.existsByUsername(registrationUserDto.getUsername())) {
            throw new DuplicateUsernameException("Пользователь с таким именем уже существует");
        }
    }

}