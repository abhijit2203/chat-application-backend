package com.trifler.chat_application_backend.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequestDTO {
	
	    @NotBlank
	    @Size(max = 5000)
	    private String content;

}
