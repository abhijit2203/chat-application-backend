package com.trifler.chat_application_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trifler.chat_application_backend.DTO.LoginRequestDTO;
import com.trifler.chat_application_backend.DTO.LoginResponseDTO;
import com.trifler.chat_application_backend.DTO.RegisterRequestDTO;
import com.trifler.chat_application_backend.DTO.UserResponseDTO;
import com.trifler.chat_application_backend.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	   
	    private final AuthService authService;
	    
	    // Register new user
	    @PostMapping("/register")
	    public ResponseEntity<UserResponseDTO> registerUser( @Valid @RequestBody RegisterRequestDTO request) {

	        UserResponseDTO response = authService.registerUser(request);
	        return ResponseEntity.status(HttpStatus.CREATED).body(response);
	    }

	    // Login existing user
	    @PostMapping("/login")
	    public ResponseEntity<LoginResponseDTO> login( @Valid @RequestBody LoginRequestDTO request) {
	        LoginResponseDTO response = authService.login(request);
            return ResponseEntity.ok(response);
	    }

}
