package com.ritam.Hotel.Reservation.System.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtConfig {

    private static final String SECRET_KEY_STRING = "ritam#1123122@@@323242323121qsadsdsd";
    private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());

public String generateToken(String username)
{
    String token = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
            .signWith(SECRET_KEY)
            .compact();
    return token;
}



    public boolean validateToken(String token)
    {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true; // Token is valid
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: " + e.getMessage());
            return false; // Token expired
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
            return false; // Signature mismatch
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false; // Token is malformed
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
            return false; // Token unsupported
        } catch (JwtException e) {
            System.out.println("JWT exception: " + e.getMessage());
            return false; // General JWT exception
        }
    }

}


