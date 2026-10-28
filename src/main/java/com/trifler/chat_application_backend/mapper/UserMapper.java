package com.trifler.chat_application_backend.mapper;

import org.mapstruct.Mapper;

import com.trifler.chat_application_backend.DTO.LoginResponseDTO;
import com.trifler.chat_application_backend.DTO.RegisterRequestDTO;
import com.trifler.chat_application_backend.DTO.UserResponseDTO;
import com.trifler.chat_application_backend.DTO.UserSummaryDTO;
import com.trifler.chat_application_backend.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	User toUser(RegisterRequestDTO userRequestDTO);
	
	UserResponseDTO toUserResponseDTO(User user);
	
	LoginResponseDTO toLoginResponseDTO(User user);
	
	UserSummaryDTO toUserSummaryDTO(User user);
}
