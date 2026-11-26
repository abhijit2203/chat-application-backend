
package com.trifler.chat_application_backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.trifler.chat_application_backend.DTO.MessageResponseDTO;
import com.trifler.chat_application_backend.DTO.SendMessageRequestDTO;
import com.trifler.chat_application_backend.entity.Conversation;
import com.trifler.chat_application_backend.entity.ConversationMember;
import com.trifler.chat_application_backend.entity.Message;
import com.trifler.chat_application_backend.entity.User;
import com.trifler.chat_application_backend.mapper.MessageMapper;
import com.trifler.chat_application_backend.repository.ConversationMemberRepository;
import com.trifler.chat_application_backend.repository.ConversationRepository;
import com.trifler.chat_application_backend.repository.MessageRepository;
import com.trifler.chat_application_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    // Send a message
    @Transactional
    public MessageResponseDTO sendMessage(
            Long currentUserId,
            Long conversationId,
            SendMessageRequestDTO request) {

        // Check conversation exists
        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Conversation not found"));

        // Only conversation members can send messages
        boolean isMember =
                conversationMemberRepository
                        .existsByConversation_IdAndUser_Id(
                                conversationId, currentUserId);

        if (!isMember) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not a member of this conversation");
        }

        User sender = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"));

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(request.getContent().trim());

        Message savedMessage = messageRepository.save(message);

        return messageMapper.toResponse(savedMessage);
    }

    // Get messages from a conversation
    @Transactional(readOnly = true)
    public List<MessageResponseDTO> getMessages(
            Long currentUserId,
            Long conversationId) {

        // Check conversation exists
        conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Conversation not found"));

        // Only conversation members can read messages
        boolean isMember =
                conversationMemberRepository
                        .existsByConversation_IdAndUser_Id(
                                conversationId, currentUserId);

        if (!isMember) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not a member of this conversation");
        }

        return messageRepository
                .findByConversation_IdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(messageMapper::toResponse)
                .toList();
    }
}

