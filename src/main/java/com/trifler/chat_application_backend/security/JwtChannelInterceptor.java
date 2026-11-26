
package com.trifler.chat_application_backend.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import com.trifler.chat_application_backend.entity.User;
import com.trifler.chat_application_backend.repository.UserRepository;
import com.trifler.chat_application_backend.repository.ConversationMemberRepository;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ConversationMemberRepository conversationMemberRepository;

    private static final Pattern CONVERSATION_TOPIC =
            Pattern.compile("^/topic/conversations/(\\d+)$");

    private static final Pattern SEND_DESTINATION =
            Pattern.compile("^/app/conversations/(\\d+)/messages$");

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(
                        message, StompHeaderAccessor.class);

        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }

        StompCommand command = accessor.getCommand();

        if (StompCommand.CONNECT.equals(command)) {
            authenticate(accessor);
        } else if (StompCommand.SUBSCRIBE.equals(command)) {
            authorizeSubscription(accessor);
        } else if (StompCommand.SEND.equals(command)) {
            authorizeSendDestination(accessor);
        }

        return message;
    }

    private void authenticate(StompHeaderAccessor accessor) {

        String authHeader =
                accessor.getFirstNativeHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AccessDeniedException(
                    "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        try {
            String email = jwtService.extractEmail(token);

            if (email == null || email.isBlank()
                    || !jwtService.isTokenValid(token, email)) {
                throw new AccessDeniedException("Invalid JWT token");
            }

            UsernamePasswordAuthenticationToken principal =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            AuthorityUtils.createAuthorityList("ROLE_USER"));

            accessor.setUser(principal);

        } catch (JwtException | IllegalArgumentException ex) {
            throw new AccessDeniedException(
                    "Invalid or expired JWT token");
        }
    }

    private void authorizeSubscription(StompHeaderAccessor accessor) {

        String destination = accessor.getDestination();

        Matcher matcher = CONVERSATION_TOPIC.matcher(
                destination == null ? "" : destination);

        if (!matcher.matches()) {
            throw new AccessDeniedException(
                    "Unsupported subscription destination");
        }

        if (accessor.getUser() == null) {
            throw new AccessDeniedException("Authentication required");
        }

        Long conversationId = parseConversationId(matcher.group(1));

        String email = accessor.getUser().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException(
                        "Authenticated user not found"));

        boolean isMember =
                conversationMemberRepository
                        .existsByConversation_IdAndUser_Id(
                                conversationId, user.getId());

        if (!isMember) {
            throw new AccessDeniedException(
                    "You are not a member of this conversation");
        }
    }

    private void authorizeSendDestination(StompHeaderAccessor accessor) {

        if (accessor.getUser() == null) {
            throw new AccessDeniedException("Authentication required");
        }

        String destination = accessor.getDestination();

        Matcher matcher = SEND_DESTINATION.matcher(
                destination == null ? "" : destination);

        if (!matcher.matches()) {
            throw new AccessDeniedException(
                    "Unsupported message destination");
        }

        // Validate the ID format here. MessageService performs the authoritative
        // conversation existence and membership checks before saving.
        parseConversationId(matcher.group(1));
    }

    private Long parseConversationId(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new AccessDeniedException("Invalid conversation ID");
        }
    }
}

