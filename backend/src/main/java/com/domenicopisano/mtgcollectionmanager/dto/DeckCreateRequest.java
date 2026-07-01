package com.domenicopisano.mtgcollectionmanager.dto;

import com.domenicopisano.mtgcollectionmanager.model.enums.DeckFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeckCreateRequest(
        @NotBlank(message = "Deck name is required")
        String name,

        String description,

        @NotNull(message = "Deck format is required")
        DeckFormat format
) {
}