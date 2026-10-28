package com.trifler.chat_application_backend.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trifler.chat_application_backend.DTO.UserSummaryDTO;
import com.trifler.chat_application_backend.entity.User;
import com.trifler.chat_application_backend.mapper.UserMapper;
import com.trifler.chat_application_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	
	public List<UserSummaryDTO> getAllUsers(String currentUserEmail) {

        User currentUser = getCurrentUser(currentUserEmail);

        List<User> users =
                userRepository.findAllByIdNot(currentUser.getId());

        return users.stream()
                .map(userMapper::toUserSummaryDTO)
                .toList();
    }

    public List<UserSummaryDTO> searchUsers(String currentUserEmail, String query) {
        User currentUser = getCurrentUser(currentUserEmail);
        String searchText = query.trim();

        if (searchText.isEmpty()) {
            return List.of();
        }

        List<User> usernameMatches =
                userRepository.findByIdNotAndUsernameContainingIgnoreCase(
                        currentUser.getId(), searchText);

        List<User> emailMatches =
                userRepository.findByIdNotAndEmailContainingIgnoreCase(
                        currentUser.getId(), searchText);

        return java.util.stream.Stream
                .concat(usernameMatches.stream(), emailMatches.stream())
                .distinct()
                .map(userMapper::toUserSummaryDTO)
                .toList();
    }

    private User getCurrentUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
