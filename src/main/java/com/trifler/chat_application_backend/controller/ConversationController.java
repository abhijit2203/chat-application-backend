package com.trifler.chat_application_backend.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.trifler.chat_application_backend.DTO.AddGroupMemberRequestDTO;
import com.trifler.chat_application_backend.DTO.ConversationResponseDTO;
import com.trifler.chat_application_backend.DTO.CreateGroupRequestDTO;
import com.trifler.chat_application_backend.DTO.DirectConversationRequestDTO;
import com.trifler.chat_application_backend.entity.User;
import com.trifler.chat_application_backend.repository.UserRepository;
import com.trifler.chat_application_backend.service.ConversationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;
    private final UserRepository userRepository;

    // Get all conversations of the logged-in user
    @GetMapping
    public List<ConversationResponseDTO> getMyConversations(
            Authentication authentication) {

        User currentUser = getCurrentUser(authentication);

        return conversationService.getMyConversations(currentUser.getId());
    }

    // Start a direct conversation
    @PostMapping("/direct")
    @ResponseStatus(HttpStatus.CREATED)
    public ConversationResponseDTO createDirectConversation(
            @Valid @RequestBody DirectConversationRequestDTO request,
            Authentication authentication) {

        User currentUser = getCurrentUser(authentication);

        return conversationService.createDirectConversation(
                currentUser.getId(),
                request.getUserId()
        );
    }

    // Create a group
    @PostMapping("/group")
    @ResponseStatus(HttpStatus.CREATED)
    public ConversationResponseDTO createGroup(
            @Valid @RequestBody CreateGroupRequestDTO request,
            Authentication authentication) {

        User currentUser = getCurrentUser(authentication);

        return conversationService.createGroup(
                currentUser.getId(),
                request
        );
    }

    // Add a member to an existing group
    @PostMapping("/{conversationId}/members")
    public ConversationResponseDTO addGroupMember(
            @PathVariable Long conversationId,
            @Valid @RequestBody AddGroupMemberRequestDTO request,
            Authentication authentication) {

        User currentUser = getCurrentUser(authentication);

        return conversationService.addGroupMember(
                currentUser.getId(),
                conversationId,
                request.getUserId()
        );
    }

    // Resolve the logged-in user from JWT email
    private User getCurrentUser(Authentication authentication) {

        if (authentication == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authentication required");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new org.springframework.web.server.ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Logged-in user not found"));
    }
}
