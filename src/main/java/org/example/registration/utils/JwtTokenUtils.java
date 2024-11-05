package org.example.registration.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.registration.model.entity.Token;
import org.example.registration.model.entity.User;
import org.example.registration.repository.TokenRepository;
import org.example.registration.service.TokenService;
import org.example.registration.service.UserService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Data
@ConfigurationProperties(prefix = "jwt")
@RequiredArgsConstructor
public class JwtTokenUtils {


    private String secret;

    private final TokenRepository tokenRepository;

    private final TokenService tokenService;

    private final UserService userService;


    public String generateToken(UserDetails userDetails, long minutes) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", rolesList);

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + minutes * 60 * 1000);

        Optional<User> user = userService.findByUsername(userDetails.getUsername());


        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        revokeAllTokenByUser(user.get());

        tokenService.saveToken(user.get(), jwtToken);


        return jwtToken;


    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllAccessTokensByUser(user.getId());
        if (validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t -> {
            t.setIsLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }

    public boolean isValid(String token) {

        return tokenRepository
                .findByAccessToken(token)
                .map(t -> !t.getIsLoggedOut())
                .orElse(false);

    }


    public boolean isTokenExpired(String token) {
        Date expiration = getAllClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }

    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
