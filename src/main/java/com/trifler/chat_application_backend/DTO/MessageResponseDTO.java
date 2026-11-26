package com.trifler.chat_application_backend.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {
	
	    private Long id;

	    private Long conversationId;

	    private Long senderId;

	    private String senderUsername;

	    private String content;

	    private LocalDateTime createdAt;

}
