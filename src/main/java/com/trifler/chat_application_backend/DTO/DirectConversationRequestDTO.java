package com.trifler.chat_application_backend.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DirectConversationRequestDTO {
    
	    @NotNull
	    @Positive
	    private Long userId;
}
