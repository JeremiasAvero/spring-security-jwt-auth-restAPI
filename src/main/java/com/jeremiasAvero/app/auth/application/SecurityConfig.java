package com.jeremiasAvero.app.auth.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtAuthFilter jwtFilter;
	  private final AppUserDetailsService uds;
	  
	  @Bean
	  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	      .csrf(csrf -> csrf.disable())
	      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	      .authorizeHttpRequests(auth -> auth
	          .requestMatchers("/auth/**", "/actuator/health").permitAll()
	          .anyRequest().authenticated()
	      )
	      .authenticationProvider(daoAuthProvider())
	      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	  }

	  @Bean
	  AuthenticationProvider daoAuthProvider() {
	    DaoAuthenticationProvider p = new DaoAuthenticationProvider();
	    p.setUserDetailsService(uds);
	    p.setPasswordEncoder(passwordEncoder());
	    return p;
	  }

	  @Bean
	  PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

	  @Bean
	  AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
	    return cfg.getAuthenticationManager();
	  }
}
