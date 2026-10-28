package com.trifler.chat_application_backend.DTO;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGroupRequestDTO {

    @NotBlank
    @Size(max = 100)
    private String name;

    private List<Long> memberIds;
}