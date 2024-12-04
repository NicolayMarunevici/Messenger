package com.nickcourse.chat.security;

import com.nickcourse.chat.exception.UserException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
  @Value("${app.jwt-secret}")
  private String jwtSecret;
  @Value("${app-jwt-expiration-milliseconds}")
  private long jwtExpirationDate;

  //generate JWT token
  public String generateToken(Authentication authentication){
    String username = authentication.getName();

    Date currentDate = new Date();
    Date expirationDate = new Date(currentDate.getTime() + jwtExpirationDate);

    return Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(expirationDate)
        .signWith(key())
        .compact();
  }

  private SecretKey key(){ // decode secret key from aplication-properties
    return Keys.hmacShaKeyFor(
        Decoders.BASE64.decode(jwtSecret)
    );
  }

  // get username from JWT token
  public String getUsername(String token){
    Claims claims = Jwts.parser() // parse JWT token on different parts// secret key
        .verifyWith(key())
        .build()
        .parseSignedClaims(token)
        .getPayload();
    return claims.getSubject(); // get username
  }

  public boolean validateToken(String token){
    try {
      Jwts.parser()
          .verifyWith(key())
          .build()
          .parse(token);
      return true;
    } catch (MalformedJwtException exception) {
      throw new UserException("Invalid JWT token", HttpStatus.BAD_REQUEST);
    } catch (ExpiredJwtException exception) {
      throw new UserException("Expired JWT token", HttpStatus.BAD_REQUEST);
    } catch (UnsupportedJwtException exception) {
      throw new UserException("Unsupported JWT token", HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) {
      throw new UserException("JWT claims string are empty", HttpStatus.BAD_REQUEST);
    }
  }
}
