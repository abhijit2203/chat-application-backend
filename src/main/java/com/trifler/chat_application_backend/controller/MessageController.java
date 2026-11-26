
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
import org.springframework.web.server.ResponseStatusException;

import com.trifler.chat_application_backend.DTO.MessageResponseDTO;
import com.trifler.chat_application_backend.DTO.SendMessageRequestDTO;
import com.trifler.chat_application_backend.entity.User;
import com.trifler.chat_application_backend.repository.UserRepository;
import com.trifler.chat_application_backend.service.MessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/conversations/{conversationId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDTO sendMessage(
            @PathVariable Long conversationId,
            @Valid @RequestBody SendMessageRequestDTO request,
            Authentication authentication) {

        User currentUser = getCurrentUser(authentication);

        return messageService.sendMessage(
                currentUser.getId(),
                conversationId,
                request);
    }

    @GetMapping
    public List<MessageResponseDTO> getMessages(
            @PathVariable Long conversationId,
            Authentication authentication) {

        User currentUser = getCurrentUser(authentication);

        return messageService.getMessages(
                currentUser.getId(),
                conversationId);
    }

    private User getCurrentUser(Authentication authentication) {

        if (authentication == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authentication required");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Logged-in user not found"));
    }
}

