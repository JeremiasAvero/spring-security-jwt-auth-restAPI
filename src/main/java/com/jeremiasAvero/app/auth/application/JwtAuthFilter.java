package com.jeremiasAvero.app.auth.application;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
	 private final JWTService jwtService;
	  private final AppUserDetailsService uds;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

	    String authHeader = request.getHeader("Authorization");
	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	      filterChain.doFilter(request, response);
	      return;
	    }

	    String token = authHeader.substring(7);
	    String email;
	    try {
	      email = jwtService.extractUsername(token);
	    } catch (Exception ex) {
	    	filterChain.doFilter(request, response);
	      return;
	    }

	    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	      UserDetails user = uds.loadUserByUsername(email);
	      if (jwtService.isTokenValid(token, user)) {
	        var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	        SecurityContextHolder.getContext().setAuthentication(auth);
	      }
	    }

	    filterChain.doFilter(request, response);
	}

}
