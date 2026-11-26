
package com.trifler.chat_application_backend.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;

import com.trifler.chat_application_backend.DTO.MessageResponseDTO;
import com.trifler.chat_application_backend.DTO.SendMessageRequestDTO;
import com.trifler.chat_application_backend.entity.User;
import com.trifler.chat_application_backend.repository.UserRepository;
import com.trifler.chat_application_backend.service.MessageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WebSocketMessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/conversations/{conversationId}/messages")
    public void sendMessage(
            @DestinationVariable Long conversationId,
            @Valid @Payload SendMessageRequestDTO request,
            Principal principal) {

        if (principal == null || principal.getName() == null) {
            throw new AccessDeniedException("Authentication required");
        }

        User sender = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new AccessDeniedException(
                        "Authenticated user not found"));

        MessageResponseDTO savedMessage = messageService.sendMessage(
                sender.getId(),
                conversationId,
                request);

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + conversationId,
                savedMessage);
    }
}

