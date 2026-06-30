package com.domenicopisano.mtgcollectionmanager.controller;

import com.domenicopisano.mtgcollectionmanager.dto.CardSearchResponse;
import com.domenicopisano.mtgcollectionmanager.service.CardSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
public class CardSearchController {

    private final CardSearchService cardSearchService;

    public CardSearchController(CardSearchService cardSearchService) {
        this.cardSearchService = cardSearchService;
    }

    @GetMapping("/search")
    public CardSearchResponse searchCardByName(@RequestParam String name) {
        return cardSearchService.searchByName(name);
    }
}