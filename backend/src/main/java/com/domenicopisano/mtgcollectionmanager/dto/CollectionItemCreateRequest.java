package com.domenicopisano.mtgcollectionmanager.dto;

import com.domenicopisano.mtgcollectionmanager.model.enums.CardCondition;
import com.domenicopisano.mtgcollectionmanager.model.enums.CardLanguage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CollectionItemCreateRequest(
        @NotBlank(message = "External card id is required")
        String externalCardId,

        @NotBlank(message = "Card name is required")
        String name,

        String expansion,
        String rarity,
        String manaCost,
        String typeLine,
        String imageUrl,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        Integer quantity,

        @NotNull(message = "Language is required")
        CardLanguage language,

        @NotNull(message = "Condition is required")
        CardCondition condition,

        boolean foil
) {
}