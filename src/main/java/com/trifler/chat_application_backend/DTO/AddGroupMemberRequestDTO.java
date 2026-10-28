package com.trifler.chat_application_backend.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddGroupMemberRequestDTO {
	
	
	@NotNull
    @Positive
    private Long userId;

}
