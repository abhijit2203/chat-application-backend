package com.trifler.chat_application_backend.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	    private final JwtService jwtService;

	   

	    @Override
	    protected void doFilterInternal(
	            HttpServletRequest request,
	            HttpServletResponse response,
	            FilterChain filterChain)
	            throws ServletException, IOException {

	        String authHeader = request.getHeader("Authorization");

	        // No JWT token supplied
	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            filterChain.doFilter(request, response);
	            return;
	        }

	        String token = authHeader.substring(7);

	        try {
	            String email = jwtService.extractEmail(token);

	            if (email != null
	                    && SecurityContextHolder.getContext()
	                            .getAuthentication() == null
	                    && jwtService.isTokenValid(token, email)) {

	                UsernamePasswordAuthenticationToken authentication =
	                        new UsernamePasswordAuthenticationToken(
	                                email,
	                                null,
	                                Collections.emptyList()
	                        );

	                authentication.setDetails(
	                        new WebAuthenticationDetailsSource()
	                                .buildDetails(request)
	                );

	                SecurityContextHolder.getContext()
	                        .setAuthentication(authentication);
	            }

	        } catch (JwtException | IllegalArgumentException ex) {
	            SecurityContextHolder.clearContext();
	        }

	        filterChain.doFilter(request, response);
	    }
}
