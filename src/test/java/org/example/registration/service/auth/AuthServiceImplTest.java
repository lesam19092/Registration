package org.example.registration.service.auth;


import org.example.registration.model.dto.*;
import org.example.registration.model.entity.User;
import org.example.registration.service.jwt.JwtTokenService;
import org.example.registration.service.token.TokenService;
import org.example.registration.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AuthServiceImplTest {


    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;


    @Mock
    private JwtTokenService jwtTokenService;

    @Test
    void testRegisterUser() {
        RegistrationUserDto registrationUserDto = new RegistrationUserDto();
        registrationUserDto.setUsername("testUser");
        registrationUserDto.setPassword("testPassword");
        registrationUserDto.setConfirmPassword("testPassword");

        User user = new User();
        user.setId(1);
        user.setName("testUser");
        user.setEmail("testUser@example.com");

        when(userService.createNewUser(any(RegistrationUserDto.class))).thenReturn(user);
        when(userService.existsByUsername("testUser")).thenReturn(false);

        ResponseEntity<?> response = authService.createNewUser(registrationUserDto);
        UserDto userDto = (UserDto) response.getBody();


        assertAll(
                "Проверка создания нового пользователя",
                () -> {
                    assertNotNull(response);
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertNotNull(response.getBody());
                    assertEquals("testUser", userDto.getUsername());
                    verify(userService, times(1)).createNewUser(any(RegistrationUserDto.class));
                }
        );
    }

    @Test
    void testAuthenticateUser() {
        JwtRequest authRequest = new JwtRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("testPassword");
        authRequest.setRememberMe(false);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtTokenService.generateToken(userDetails, 10)).thenReturn("testToken");


        ResponseEntity<?> response = authService.createAuthToken(authRequest);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();

        assertAll(
                "Проверка аутентификации пользователя",
                () -> {
                    assertNotNull(response);
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertNotNull(jwtResponse);
                    assertEquals("testToken", jwtResponse.getToken());
                }
        );
    }

    @Test
    void testResetPassword() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setName("testUser");
        request.setNewPassword("newPassword");
        request.setConfirmPassword("newPassword");
        request.setConfirmationCode("0000");

        doNothing().when(userService).resetPassword(any(PasswordResetRequest.class));

        ResponseEntity<?> response = authService.resetPassword(request);

        assertAll(
                "Проверка сброса пароля",
                () -> {
                    assertNotNull(response);
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertEquals("Password reset successfully", response.getBody());
                    verify(userService, times(1)).resetPassword(any(PasswordResetRequest.class));
                }
        );
    }
}