package org.example.registration.repository;

import org.example.registration.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
            select t from Token t join fetch t.user
            where t.user.id = :userId and t.isLoggedOut = false
            """)
    List<Token> findAllAccessTokensByUser(Integer userId);

    Optional<Token> findByAccessToken(String token);

}