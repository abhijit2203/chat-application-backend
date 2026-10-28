package com.trifler.chat_application_backend.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
	
	    private Long id;
	    private String username;
	    private String email;
	    private String avatarUrl;
	    private LocalDateTime createdAt;

}
