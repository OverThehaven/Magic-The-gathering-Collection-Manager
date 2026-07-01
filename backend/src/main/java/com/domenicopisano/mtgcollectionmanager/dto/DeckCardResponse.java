package com.domenicopisano.mtgcollectionmanager.dto;

import com.domenicopisano.mtgcollectionmanager.model.enums.DeckCardSection;

public record DeckCardResponse(
        Long id,
        Long collectionItemId,
        String cardName,
        String expansion,
        String manaCost,
        String typeLine,
        String imageUrl,
        int quantity,
        DeckCardSection section
) {
}