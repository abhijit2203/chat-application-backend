package com.trifler.chat_application_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
	
	    private String token;
	    private String tokenType;
	    private Long id;
	    private String username;
	    private String email;

}
