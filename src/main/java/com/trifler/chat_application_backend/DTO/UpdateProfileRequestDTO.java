package com.trifler.chat_application_backend.DTO;

import com.trifler.chat_application_backend.entity.Gender;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class UpdateProfileRequestDTO {

    @Size(min = 3, max = 50,
          message = "Username must be between 3 and 50 characters")
    private String username;

    @Pattern(
        regexp = "^$|^[+]?[0-9() -]{7,20}$",
        message = "Please enter a valid phone number"
    )
    private String phone;

    private Gender gender;

    @Size(max = 500,
          message = "Bio cannot exceed 500 characters")
    private String bio;

    private String avatarUrl;

    @AssertTrue(message = "Username cannot be blank")
    public boolean isUsernameValid() {
        return username == null || !username.isBlank();
    }
}