package com.domenicopisano.mtgcollectionmanager.service;

import com.domenicopisano.mtgcollectionmanager.dto.CardSearchResponse;
import com.domenicopisano.mtgcollectionmanager.exception.BadRequestException;
import com.domenicopisano.mtgcollectionmanager.external.ScryfallCardResponse;
import com.domenicopisano.mtgcollectionmanager.external.ScryfallClient;
import org.springframework.stereotype.Service;

@Service
public class CardSearchService {

    private final ScryfallClient scryfallClient;

    public CardSearchService(ScryfallClient scryfallClient) {
        this.scryfallClient = scryfallClient;
    }

    public CardSearchResponse searchByName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Card name is required");
        }

        ScryfallCardResponse card = scryfallClient.findCardByName(name.trim());

        return new CardSearchResponse(
                card.id(),
                card.name(),
                card.setName(),
                card.rarity(),
                card.manaCost(),
                card.typeLine(),
                card.oracleText(),
                card.bestImageUrl()
        );
    }
}