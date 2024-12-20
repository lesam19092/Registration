package org.example.registration.service.jwt;

import org.example.registration.service.auth.AuthServiceImpl;
import org.example.registration.service.token.TokenService;
import org.example.registration.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;


    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtTokenServiceImpl jwtTokenService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenService, "secret", "your-base64-encoded-secret-key");
    }

    @Test
    void generateToken() {
        String token = jwtTokenService.generateToken(userDetails, 60);

        assertNotNull(token);
    }
}