package com.trifler.chat_application_backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.trifler.chat_application_backend.DTO.ConversationResponseDTO;
import com.trifler.chat_application_backend.entity.Conversation;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

    // Map using the name stored in Conversation entity
    @Mapping(target = "createdBy", source = "createdBy.id")
    ConversationResponseDTO toResponse(Conversation conversation);

    // Map using a custom display name
    @Mapping(target = "createdBy", source = "conversation.createdBy.id")
    @Mapping(target = "name", source = "displayName")
    ConversationResponseDTO toResponse(
            Conversation conversation,
            String displayName
    );
}