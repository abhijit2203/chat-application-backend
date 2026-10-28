package com.trifler.chat_application_backend.DTO;

import java.time.LocalDateTime;

import com.trifler.chat_application_backend.entity.ConversationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResponseDTO {
	
	    private Long id;

	    private ConversationType type;

	    private String name;

	    private Long createdBy;

	    private LocalDateTime createdAt;

}
