package com.jeremiasAvero.app.auth.application;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;



@Service
public class JWTService {

	@Value("${app.security.jwt.secret}")
	private String secretBase64;
	
	@Value("${app.security.jwt.expiration-ms}")
	private long expirationMs;
	
	private SecretKey key;
	
	@PostConstruct
	void init() {
		key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64));
	}
	
	public String generateToken(UserDetails user) {
	    final Instant now = Instant.now();
	    return Jwts.builder()
	        .subject(user.getUsername()) // email
	        .claim("roles", user.getAuthorities().stream()
	            .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
	        .issuedAt(Date.from(now))
	        .expiration(Date.from(now.plusMillis(expirationMs)))
	        .signWith(key, Jwts.SIG.HS256)
	        .compact();
	  }

	  public String extractUsername(String token) {
	    return Jwts.parser().verifyWith(key).build()
	        .parseSignedClaims(token).getPayload().getSubject();
	  }

	  public boolean isTokenValid(String token, UserDetails user) {
	    var claims = Jwts.parser().verifyWith(key).build()
	        .parseSignedClaims(token).getPayload();
	    String subject = claims.getSubject();
	    Date exp = claims.getExpiration();
	    return subject.equals(user.getUsername()) && exp != null && exp.after(new Date());
	  }
}
