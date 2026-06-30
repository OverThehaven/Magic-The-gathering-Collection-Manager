package com.domenicopisano.mtgcollectionmanager.dto;

public record CardSearchResponse(
        String externalCardId,
        String name,
        String expansion,
        String rarity,
        String manaCost,
        String typeLine,
        String oracleText,
        String imageUrl
) {
}