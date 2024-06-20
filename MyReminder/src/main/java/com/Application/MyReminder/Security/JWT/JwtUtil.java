package com.Application.MyReminder.Security.JWT;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
    final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
       return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolve) {
        final Claims claims = extractAllClaim(token);
        return claimsResolve.apply(claims);
    }

    private Claims extractAllClaim(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        System.out.println(key);
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date((System.currentTimeMillis())))
                .setExpiration(new Date(System.currentTimeMillis()*1000 *60*60 *10))
                .signWith(key).compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String name = extractUserName(token);
        return (name.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
