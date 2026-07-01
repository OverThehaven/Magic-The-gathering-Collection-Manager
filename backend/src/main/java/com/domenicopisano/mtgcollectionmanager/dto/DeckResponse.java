package com.domenicopisano.mtgcollectionmanager.dto;

import com.domenicopisano.mtgcollectionmanager.model.enums.DeckFormat;

import java.time.LocalDateTime;

public record DeckResponse(
        Long id,
        String name,
        String description,
        DeckFormat format,
        Long ownerId,
        Long totalCards,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}