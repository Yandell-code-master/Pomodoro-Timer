package com.backend.code.pomodoro_timer.repository;


import com.backend.code.pomodoro_timer.model.Token;
import jakarta.persistence.EntityManager;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class TokenCustomRepositoryImpl implements TokenCustomRepository{

    private final EntityManager entityManager;


    public TokenCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Token> findByToken(String tokenToFind) {

        // I use a stream to make the function very much efficient
        return entityManager.createQuery("SELECT t FROM Token t", Token.class)
                .getResultStream()
                .filter(token -> BCrypt.checkpw(tokenToFind, token.getToken()))
                .findFirst();
    }
}
