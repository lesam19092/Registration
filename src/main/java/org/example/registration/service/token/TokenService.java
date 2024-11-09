package org.example.registration.service.token;

import org.example.registration.model.entity.Token;
import org.example.registration.model.entity.User;

public interface TokenService {

    Token saveToken(User user, String token);

    void revokeAllTokenByUser(User user);

    boolean isValid(String token);

}

