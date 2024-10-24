package com.ypay.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ypay.api.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${jwt.secret}")
    private String secretJWT;

    private final String ISSUER = "auth-api";

    public String generateToken(User user) {
        try {
            return JWT
                    .create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getEmail())
                    .withExpiresAt(generateTokenExpirationDate())
                    .sign(getJWTAlgorithm());

        } catch (JWTCreationException jwtCreationException) {
            return null;
        }
    }

    public String validateToken(String token) {
        try {
            return JWT
                    .require(getJWTAlgorithm())
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException jwtVerificationException) {
            return null;
        }
    }

    private Instant generateTokenExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    private Algorithm getJWTAlgorithm() {
        return Algorithm.HMAC256(secretJWT);
    }
}
