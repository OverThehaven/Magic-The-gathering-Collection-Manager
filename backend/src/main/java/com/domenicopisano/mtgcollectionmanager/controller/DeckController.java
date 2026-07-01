package com.domenicopisano.mtgcollectionmanager.controller;

import com.domenicopisano.mtgcollectionmanager.dto.DeckCardAddRequest;
import com.domenicopisano.mtgcollectionmanager.dto.DeckCardResponse;
import com.domenicopisano.mtgcollectionmanager.dto.DeckCreateRequest;
import com.domenicopisano.mtgcollectionmanager.dto.DeckResponse;
import com.domenicopisano.mtgcollectionmanager.dto.DeckUpdateRequest;
import com.domenicopisano.mtgcollectionmanager.model.enums.DeckFormat;
import com.domenicopisano.mtgcollectionmanager.service.DeckService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{ownerId}/decks")
public class DeckController {

    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeckResponse createDeck(
            @PathVariable Long ownerId,
            @Valid @RequestBody DeckCreateRequest request
    ) {
        return deckService.createDeck(ownerId, request);
    }

    @GetMapping
    public Page<DeckResponse> getDecks(
            @PathVariable Long ownerId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) DeckFormat format,
            @PageableDefault(size = 10, sort = "name") Pageable pageable
    ) {
        return deckService.getDecks(ownerId, name, format, pageable);
    }

    @GetMapping("/{deckId}")
    public DeckResponse getDeckById(
            @PathVariable Long ownerId,
            @PathVariable Long deckId
    ) {
        return deckService.getDeckById(ownerId, deckId);
    }

    @PatchMapping("/{deckId}")
    public DeckResponse updateDeck(
            @PathVariable Long ownerId,
            @PathVariable Long deckId,
            @Valid @RequestBody DeckUpdateRequest request
    ) {
        return deckService.updateDeck(ownerId, deckId, request);
    }

    @DeleteMapping("/{deckId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeck(
            @PathVariable Long ownerId,
            @PathVariable Long deckId
    ) {
        deckService.deleteDeck(ownerId, deckId);
    }

    @PostMapping("/{deckId}/cards")
    @ResponseStatus(HttpStatus.CREATED)
    public DeckCardResponse addCardToDeck(
            @PathVariable Long ownerId,
            @PathVariable Long deckId,
            @Valid @RequestBody DeckCardAddRequest request
    ) {
        return deckService.addCardToDeck(ownerId, deckId, request);
    }

    @GetMapping("/{deckId}/cards")
    public List<DeckCardResponse> getDeckCards(
            @PathVariable Long ownerId,
            @PathVariable Long deckId
    ) {
        return deckService.getDeckCards(ownerId, deckId);
    }

    @DeleteMapping("/{deckId}/cards/{deckCardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCardFromDeck(
            @PathVariable Long ownerId,
            @PathVariable Long deckId,
            @PathVariable Long deckCardId
    ) {
        deckService.removeCardFromDeck(ownerId, deckId, deckCardId);
    }
}