package com.art.ai_interview_platform_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtService {
    private final String JWT_SECREATE_KEY="mysecreatekeyisonetwothreeandfourwithotherfriendsareplaying";
    private final Integer JWT_EXPIRATION = 3000000;
    public String getEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isExpiration(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date(System.currentTimeMillis()));
    }

    public <T>T extractClaim(String token, Function<Claims, T> resolver){
        Claims allClaims = extractAllClaims(token);
        return resolver.apply(allClaims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(secretKey())
                .compact();
    }


    public boolean isTokenValid(String token, UserDetails user) {
        return !isExpiration(token) && user.getUsername().equals(getEmail(token));
    }

    private SecretKey secretKey(){
        byte []bytes = Decoders.BASE64.decode(JWT_SECREATE_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }
}
