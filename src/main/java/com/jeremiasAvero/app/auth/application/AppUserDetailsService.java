package com.jeremiasAvero.app.auth.application;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jeremiasAvero.app.auth.domain.UserEntity;
import com.jeremiasAvero.app.auth.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

  private final UserRepository users;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserEntity u = users.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("email not found"));
    return org.springframework.security.core.userdetails.User
        .withUsername(u.getEmail())
        .password(u.getPassword())
        .roles(u.getRole()) // converts to "ROLE_<role>"
        .build();
  }
}
