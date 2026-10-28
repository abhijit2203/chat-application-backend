package com.trifler.chat_application_backend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trifler.chat_application_backend.entity.User;
import com.trifler.chat_application_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		 User user = userRepository.findByEmail(email)
				 .orElseThrow(() ->new UsernameNotFoundException("User not found with email: " + email));
		 
		 UserDetails user2 = org.springframework.security.core.userdetails.User
			        .withUsername(user.getEmail())
			        .password(user.getPassword())
			        .authorities("ROLE_USER")
			        .build();

      return user2;
	}

}
