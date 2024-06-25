package com.example.QuestionPortalBackend.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JWTUtil {

    @Value("${jwt-secret}")
    private String secret;

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Date expirationDate = Date.from(ZonedDateTime.now().plusDays(7).toInstant());
        return JWT.create()
                .withSubject("User details")
                .withClaim("email", userDetails.getUsername())
                .withClaim("password", userDetails.getPassword())
                .withIssuedAt(new Date())
                .withIssuer("question-portal-backend")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public Map<String, String> validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("question-portal-backend")
                .build();

        DecodedJWT jwt = jwtVerifier.verify(token);
        Map<String, String> claims = new HashMap<>();
        claims.put("email", jwt.getClaim("email").asString());
        claims.put("password", jwt.getClaim("password").asString());
        return claims;
    }
}
