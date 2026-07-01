package com.domenicopisano.mtgcollectionmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MessageCreateRequest(
        @NotNull(message = "Receiver id is required")
        Long receiverId,

        @NotBlank(message = "Message content is required")
        @Size(max = 1000, message = "Message content cannot exceed 1000 characters")
        String content
) {
}