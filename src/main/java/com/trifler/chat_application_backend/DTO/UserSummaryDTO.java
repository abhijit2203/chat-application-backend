package com.trifler.chat_application_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryDTO {
	
	    private Long id;
	    private String username;
	    private String avatarUrl;

}
