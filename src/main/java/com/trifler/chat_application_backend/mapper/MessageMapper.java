package com.trifler.chat_application_backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.trifler.chat_application_backend.DTO.MessageResponseDTO;
import com.trifler.chat_application_backend.entity.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "conversationId", source = "conversation.id")
    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "senderUsername", source = "sender.username")
    MessageResponseDTO toResponse(Message message);
}
