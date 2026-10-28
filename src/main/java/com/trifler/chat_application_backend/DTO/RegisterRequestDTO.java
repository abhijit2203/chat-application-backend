package com.trifler.chat_application_backend.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
	
	
	    @NotBlank(message = "Username is required")
	    @Size(min = 3, max = 50,
	          message = "Username must be between 3 and 50 characters")
	    private String username;

	    @NotBlank(message = "Email is required")
	    @Email(message = "Please enter a valid email")
	    @Size(max = 100)
	    private String email;

	    @NotBlank(message = "Password is required")
	    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
	    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^a-zA-Z0-9\\s]).{8,16}$",
	        message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
	    )
	    private String password;

}
