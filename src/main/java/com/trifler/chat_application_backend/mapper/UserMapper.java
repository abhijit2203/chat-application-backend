package com.trifler.chat_application_backend.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.trifler.chat_application_backend.DTO.LoginResponseDTO;
import com.trifler.chat_application_backend.DTO.RegisterRequestDTO;
import com.trifler.chat_application_backend.DTO.UpdateProfileRequestDTO;
import com.trifler.chat_application_backend.DTO.UserResponseDTO;
import com.trifler.chat_application_backend.DTO.UserSummaryDTO;
import com.trifler.chat_application_backend.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Registration
    User toUser(RegisterRequestDTO userRequestDTO);

    // User response
    UserResponseDTO toUserResponseDTO(User user);

    // Login response
    LoginResponseDTO toLoginResponseDTO(User user);

    // User summary
    UserSummaryDTO toUserSummaryDTO(User user);

    // Update existing user profile
    @BeanMapping(
    	    nullValuePropertyMappingStrategy =
    	        NullValuePropertyMappingStrategy.IGNORE
    	)
    	void updateUserFromDTO(
    	        UpdateProfileRequestDTO dto,
    	        @MappingTarget User user
    	);
}
