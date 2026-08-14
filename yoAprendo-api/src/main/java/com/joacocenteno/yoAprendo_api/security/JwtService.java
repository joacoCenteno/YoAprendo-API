package com.joacocenteno.yoAprendo_api.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.joacocenteno.yoAprendo_api.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private String secretKey = "Joaco130534234";
    private long jwtExpiration = 86400000;

    public String extractUserPlatformName(String token){
        return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
    }

    public String generateToken(final User user){
        return buildToken(user, jwtExpiration);
    }

    public String buildToken(final User user, final long expiration){
        return Jwts
                .builder()
                .claim("role", user.getUserRol().name())
                .subject(user.getUserPlatformName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, User user){
        final String username = extractUserPlatformName(token);
        return (username.equals(user.getUserPlatformName())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
    }

    private SecretKey getSignInKey(){
        final byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
