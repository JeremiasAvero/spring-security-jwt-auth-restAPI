package com.jeremiasAvero.app.auth.infraestructure.web.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jeremiasAvero.app.auth.application.AuthService;
import com.jeremiasAvero.app.auth.infraestructure.dto.AuthResponse;
import com.jeremiasAvero.app.auth.infraestructure.dto.LoginRequest;
import com.jeremiasAvero.app.auth.infraestructure.dto.RegisterRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService auth;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
    String token = auth.register(req);
    return ResponseEntity.ok(new AuthResponse(token));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
    String token = auth.login(req);
    return ResponseEntity.ok(new AuthResponse(token));
  }
}