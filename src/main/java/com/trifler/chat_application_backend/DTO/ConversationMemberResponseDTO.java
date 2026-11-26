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
public class ConversationMemberResponseDTO {

    private Long userId;
    private String username;
    private String avatarUrl;
    private LocalDateTime joinedAt;
}
