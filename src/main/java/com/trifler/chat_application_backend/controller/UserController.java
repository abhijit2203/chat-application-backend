package com.trifler.chat_application_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trifler.chat_application_backend.DTO.UserSummaryDTO;
import com.trifler.chat_application_backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	    @GetMapping
	    public ResponseEntity<List<UserSummaryDTO>> getAllUsers(
	            Authentication authentication) {

	        List<UserSummaryDTO> users =
	                userService.getAllUsers(authentication.getName());

	        return ResponseEntity.ok(users);
	    }

	    @GetMapping("/search")
	    public ResponseEntity<List<UserSummaryDTO>> searchUsers(@RequestParam String query,Authentication authentication) {
	        List<UserSummaryDTO> users =
	                userService.searchUsers(
	                        authentication.getName(), query);

	        return ResponseEntity.ok(users);
	    }
	

}
