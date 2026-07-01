package com.domenicopisano.mtgcollectionmanager.dto;

import com.domenicopisano.mtgcollectionmanager.model.enums.DeckCardSection;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DeckCardAddRequest(
        @NotNull(message = "Collection item id is required")
        Long collectionItemId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        Integer quantity,

        @NotNull(message = "Deck card section is required")
        DeckCardSection section
) {
}