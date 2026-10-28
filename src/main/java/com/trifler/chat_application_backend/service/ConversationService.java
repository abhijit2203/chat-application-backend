package com.trifler.chat_application_backend.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.trifler.chat_application_backend.DTO.ConversationResponseDTO;
import com.trifler.chat_application_backend.DTO.CreateGroupRequestDTO;
import com.trifler.chat_application_backend.entity.Conversation;
import com.trifler.chat_application_backend.entity.ConversationMember;
import com.trifler.chat_application_backend.entity.ConversationType;
import com.trifler.chat_application_backend.entity.User;
import com.trifler.chat_application_backend.mapper.ConversationMapper;
import com.trifler.chat_application_backend.repository.ConversationMemberRepository;
import com.trifler.chat_application_backend.repository.ConversationRepository;
import com.trifler.chat_application_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final UserRepository userRepository;
    private final ConversationMapper conversationMapper;


    // 1. Get all conversations of logged-in user
    @Transactional(readOnly = true)
    public List<ConversationResponseDTO> getMyConversations(Long userId) {

        List<ConversationMember> memberships =
                conversationMemberRepository.findByUser_Id(userId);

        List<ConversationResponseDTO> responses = new ArrayList<>();

        for (ConversationMember membership : memberships) {

            Conversation conversation = membership.getConversation();

            // Group name comes from the Conversation entity.
            String displayName = conversation.getName();

            // For direct chat, display the other user's username.
            if (conversation.getType() == ConversationType.DIRECT) {

                List<ConversationMember> members =
                        conversationMemberRepository
                                .findByConversation_Id(conversation.getId());

                displayName = members.stream()
                        .map(ConversationMember::getUser)
                        .filter(user -> !user.getId().equals(userId))
                        .map(User::getUsername)
                        .findFirst()
                        .orElse("Unknown user");
            }

            // Map entity to DTO using MapStruct.
            responses.add(
                    conversationMapper.toResponse(
                            conversation,
                            displayName
                    )
            );
        }

        return responses;
    }


    // 2. Create or return an existing direct conversation
    @Transactional
    public ConversationResponseDTO createDirectConversation(
            Long currentUserId,
            Long targetUserId) {

        if (currentUserId.equals(targetUserId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You cannot start a conversation with yourself"
            );
        }

        User currentUser = getUser(currentUserId);
        User targetUser = getUser(targetUserId);

        List<ConversationMember> currentMemberships =
                conversationMemberRepository.findByUser_Id(currentUserId);

        for (ConversationMember membership : currentMemberships) {

            Conversation conversation = membership.getConversation();

            if (conversation.getType() != ConversationType.DIRECT) {
                continue;
            }

            List<ConversationMember> members =
                    conversationMemberRepository
                            .findByConversation_Id(conversation.getId());

            boolean targetIsMember = members.stream()
                    .anyMatch(member ->
                            member.getUser().getId().equals(targetUserId));

            if (targetIsMember && members.size() == 2) {

                return conversationMapper.toResponse(
                        conversation,
                        targetUser.getUsername()
                );
            }
        }

        // Create a new direct conversation.
        Conversation conversation = new Conversation();
        conversation.setType(ConversationType.DIRECT);
        conversation.setCreatedBy(currentUser);

        Conversation savedConversation =
                conversationRepository.save(conversation);

        // Add both users as members.
        addMember(savedConversation, currentUser);
        addMember(savedConversation, targetUser);

        return conversationMapper.toResponse(
                savedConversation,
                targetUser.getUsername()
        );
    }


    // 3. Create a group and automatically add its creator
    @Transactional
    public ConversationResponseDTO createGroup(
            Long currentUserId,
            CreateGroupRequestDTO request) {

        User creator = getUser(currentUserId);

        Set<Long> memberIds = new LinkedHashSet<>();

        if (request.getMemberIds() != null) {

            for (Long memberId : request.getMemberIds()) {

                if (memberId == null || memberId <= 0) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Invalid member ID"
                    );
                }

                memberIds.add(memberId);
            }
        }

        // Creator is added separately, automatically.
        memberIds.remove(currentUserId);

        List<User> selectedUsers = new ArrayList<>();

        for (Long memberId : memberIds) {
            selectedUsers.add(getUser(memberId));
        }

        Conversation conversation = new Conversation();
        conversation.setType(ConversationType.GROUP);
        conversation.setName(request.getName().trim());
        conversation.setCreatedBy(creator);

        Conversation savedConversation =
                conversationRepository.save(conversation);

        // Add creator by default.
        addMember(savedConversation, creator);

        // Add selected members.
        for (User user : selectedUsers) {
            addMember(savedConversation, user);
        }

        return conversationMapper.toResponse(
                savedConversation,
                savedConversation.getName()
        );
    }


    // 4. Add a member to an existing group
    @Transactional
    public ConversationResponseDTO addGroupMember(
            Long currentUserId,
            Long conversationId,
            Long newMemberId) {

        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Conversation not found"
                ));

        if (conversation.getType() != ConversationType.GROUP) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Members can only be added to groups"
            );
        }

        // Only the group creator can add members.
        if (!conversation.getCreatedBy().getId().equals(currentUserId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only the group creator can add members"
            );
        }

        User newMember = getUser(newMemberId);

        boolean alreadyMember =
                conversationMemberRepository
                        .existsByConversation_IdAndUser_Id(
                                conversationId,
                                newMemberId
                        );

        if (alreadyMember) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User is already a group member"
            );
        }

        addMember(conversation, newMember);

        return conversationMapper.toResponse(
                conversation,
                conversation.getName()
        );
    }


    // Helper: find a registered user
    private User getUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found: " + userId
                ));
    }


    // Helper: create membership
    private void addMember(Conversation conversation, User user) {

        ConversationMember member = new ConversationMember();
        member.setConversation(conversation);
        member.setUser(user);

        conversationMemberRepository.save(member);
    }
}
