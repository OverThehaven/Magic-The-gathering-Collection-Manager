package com.domenicopisano.mtgcollectionmanager.dto;

import com.domenicopisano.mtgcollectionmanager.model.enums.CardCondition;
import com.domenicopisano.mtgcollectionmanager.model.enums.CardLanguage;

public record CollectionItemResponse(
        Long id,
        String externalCardId,
        String name,
        String expansion,
        String rarity,
        String manaCost,
        String typeLine,
        String imageUrl,
        int quantity,
        CardLanguage language,
        CardCondition condition,
        boolean foil,
        Long ownerId
) {
}