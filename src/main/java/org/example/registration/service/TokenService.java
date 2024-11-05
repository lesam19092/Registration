package org.example.registration.service;


import lombok.RequiredArgsConstructor;
import org.example.registration.model.entity.Token;
import org.example.registration.model.entity.User;
import org.example.registration.repository.TokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;


    public Token saveToken(User user, String token) {
        Token tokenEntity = new Token();
        tokenEntity.setAccessToken(token);
        tokenEntity.setIsLoggedOut(false);
        tokenEntity.setUser(user);

        tokenRepository.save(tokenEntity);

        return tokenEntity;
    }
}
