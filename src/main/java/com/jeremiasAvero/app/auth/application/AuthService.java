package com.jeremiasAvero.app.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jeremiasAvero.app.auth.domain.UserEntity;
import com.jeremiasAvero.app.auth.domain.UserRepository;
import com.jeremiasAvero.app.auth.infraestructure.dto.LoginRequest;
import com.jeremiasAvero.app.auth.infraestructure.dto.RegisterRequest;
@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authManager;
  private final JWTService jwt;

  public String register(RegisterRequest req) {
    if (users.existsByEmail(req.email()))
      throw new IllegalArgumentException("Email already registered");

    UserEntity u = users.save(UserEntity.builder()
        .email(req.email().toLowerCase())
        .password(encoder.encode(req.password()))
        .role("USER")
        .build());

    UserDetails ud = org.springframework.security.core.userdetails.User
        .withUsername(u.getEmail()).password(u.getPassword()).roles(u.getRole()).build();
    return jwt.generateToken(ud);
  }

  public String login(LoginRequest req) {
    var authToken = new UsernamePasswordAuthenticationToken(req.email(), req.password());
    authManager.authenticate(authToken); // throws if invalid
    // Load to include authorities in token
    UserDetails ud = org.springframework.security.core.userdetails.User
        .withUsername(req.email()).password("N/A").roles("USER").build(); // role will be reloaded via filter anyway
    return jwt.generateToken(ud);
  }
}
