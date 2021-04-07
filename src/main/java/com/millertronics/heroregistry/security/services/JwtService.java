package com.millertronics.heroregistry.security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String secret = "s3cret!";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJwt(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        return expirationDate.after(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        final long currentTimestamp = System.currentTimeMillis();
        final long expirationOffset = 1000 * 60 * 60 * 10;

        return Jwts.builder().setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(currentTimestamp))
                .setExpiration(new Date(currentTimestamp + expirationOffset))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


}
