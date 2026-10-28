package com.trifler.chat_application_backend.service;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trifler.chat_application_backend.DTO.LoginRequestDTO;
import com.trifler.chat_application_backend.DTO.LoginResponseDTO;
import com.trifler.chat_application_backend.DTO.RegisterRequestDTO;
import com.trifler.chat_application_backend.DTO.UserResponseDTO;
import com.trifler.chat_application_backend.entity.User;
import com.trifler.chat_application_backend.mapper.UserMapper;
import com.trifler.chat_application_backend.repository.UserRepository;
import com.trifler.chat_application_backend.security.JwtService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	    private final UserMapper userMapper;
	    private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;
	    private final JwtService jwtService;
	    
	    @Transactional
	    public UserResponseDTO registerUser(RegisterRequestDTO request) {

	        String username = request.getUsername().trim();
	        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

	        if (userRepository.existsByEmail(email)) {
	            throw new IllegalArgumentException("Email is already registered");
	        }

	        if (userRepository.existsByUsername(username)) {
	            throw new IllegalArgumentException("Username is already taken");
	        }

	        // Step 1: DTO to Entity
	        User user = userMapper.toUser(request);

	        // Step 2: Set normalized values
	        user.setUsername(username);
	        user.setEmail(email);

	        // Step 3: Encode password
	        user.setPassword(passwordEncoder.encode(request.getPassword()));

	        // Step 4: Set timestamps
	        LocalDateTime now = LocalDateTime.now();
	        user.setCreatedAt(now);
	        user.setUpdatedAt(now);

	        // Step 5: Save Entity to database
	        User savedUser = userRepository.save(user);

	        // Step 6: Entity to DTO
	        return userMapper.toUserResponseDTO(savedUser);
	    }
	    
	    public LoginResponseDTO login(LoginRequestDTO request) {

	        String email = request.getEmail()
	                .trim()
	                .toLowerCase(Locale.ROOT);

	        // 1. Fetch existing user from database
	        User user = userRepository.findByEmail(email)
	                .orElseThrow(() ->
	                    new BadCredentialsException("Invalid email or password")
	                );

	        // 2. Verify password
	        boolean passwordMatches = passwordEncoder.matches(
	                request.getPassword(),
	                user.getPassword()
	        );

	        if (!passwordMatches) {
	            throw new BadCredentialsException("Invalid email or password");
	        }

	        // 3. Generate JWT
	        String token = jwtService.generateToken(user.getEmail());

	        // 4. Entity to LoginResponseDTO
	        LoginResponseDTO response = userMapper.toLoginResponseDTO(user);

	        // 5. Set generated token details
	        response.setToken(token);
	        response.setTokenType("Bearer");

	        return response;
	    }

}
