package com.domenicopisano.mtgcollectionmanager.service;

import com.domenicopisano.mtgcollectionmanager.dto.DeckCardAddRequest;
import com.domenicopisano.mtgcollectionmanager.dto.DeckCardResponse;
import com.domenicopisano.mtgcollectionmanager.dto.DeckCreateRequest;
import com.domenicopisano.mtgcollectionmanager.dto.DeckResponse;
import com.domenicopisano.mtgcollectionmanager.dto.DeckUpdateRequest;
import com.domenicopisano.mtgcollectionmanager.exception.BadRequestException;
import com.domenicopisano.mtgcollectionmanager.exception.ResourceNotFoundException;
import com.domenicopisano.mtgcollectionmanager.model.entity.CollectionItem;
import com.domenicopisano.mtgcollectionmanager.model.entity.Deck;
import com.domenicopisano.mtgcollectionmanager.model.entity.DeckCard;
import com.domenicopisano.mtgcollectionmanager.model.entity.UserProfile;
import com.domenicopisano.mtgcollectionmanager.model.enums.DeckCardSection;
import com.domenicopisano.mtgcollectionmanager.model.enums.DeckFormat;
import com.domenicopisano.mtgcollectionmanager.repository.DeckCardRepository;
import com.domenicopisano.mtgcollectionmanager.repository.DeckRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeckService {

    private final DeckRepository deckRepository;
    private final DeckCardRepository deckCardRepository;
    private final UserProfileService userProfileService;
    private final CollectionItemService collectionItemService;

    public DeckService(
            DeckRepository deckRepository,
            DeckCardRepository deckCardRepository,
            UserProfileService userProfileService,
            CollectionItemService collectionItemService
    ) {
        this.deckRepository = deckRepository;
        this.deckCardRepository = deckCardRepository;
        this.userProfileService = userProfileService;
        this.collectionItemService = collectionItemService;
    }

    @Transactional
    public DeckResponse createDeck(Long ownerId, DeckCreateRequest request) {
        UserProfile owner = userProfileService.getUserEntityById(ownerId);

        Deck deck = new Deck();
        deck.setOwner(owner);
        deck.setName(request.name());
        deck.setDescription(request.description());
        deck.setFormat(request.format());

        Deck savedDeck = deckRepository.save(deck);
        return toDeckResponse(savedDeck);
    }

    @Transactional(readOnly = true)
    public Page<DeckResponse> getDecks(Long ownerId, String name, DeckFormat format, Pageable pageable) {
        userProfileService.getUserEntityById(ownerId);

        Page<Deck> decks;

        if (name != null && !name.isBlank()) {
            decks = deckRepository.findByOwnerIdAndNameContainingIgnoreCase(ownerId, name, pageable);
        } else if (format != null) {
            decks = deckRepository.findByOwnerIdAndFormat(ownerId, format, pageable);
        } else {
            decks = deckRepository.findByOwnerId(ownerId, pageable);
        }

        return decks.map(this::toDeckResponse);
    }

    @Transactional(readOnly = true)
    public DeckResponse getDeckById(Long ownerId, Long deckId) {
        Deck deck = getDeckEntityByIdAndOwnerId(ownerId, deckId);
        return toDeckResponse(deck);
    }

    @Transactional
    public DeckResponse updateDeck(Long ownerId, Long deckId, DeckUpdateRequest request) {
        Deck deck = getDeckEntityByIdAndOwnerId(ownerId, deckId);

        deck.setName(request.name());
        deck.setDescription(request.description());
        deck.setFormat(request.format());

        return toDeckResponse(deck);
    }

    @Transactional
    public void deleteDeck(Long ownerId, Long deckId) {
        Deck deck = getDeckEntityByIdAndOwnerId(ownerId, deckId);
        deckRepository.delete(deck);
    }

    @Transactional
    public DeckCardResponse addCardToDeck(Long ownerId, Long deckId, DeckCardAddRequest request) {
        Deck deck = getDeckEntityByIdAndOwnerId(ownerId, deckId);
        CollectionItem collectionItem = collectionItemService.getItemEntityByIdAndOwnerId(
                ownerId,
                request.collectionItemId()
        );

        validateCommanderRules(deck, request);

        return deckCardRepository
                .findByDeckIdAndCollectionItemIdAndSection(
                        deckId,
                        request.collectionItemId(),
                        request.section()
                )
                .map(existingCard -> {
                    int newQuantity = existingCard.getQuantity() + request.quantity();
                    validateQuantityAgainstCollection(newQuantity, collectionItem);
                    existingCard.setQuantity(newQuantity);
                    return toDeckCardResponse(existingCard);
                })
                .orElseGet(() -> {
                    validateQuantityAgainstCollection(request.quantity(), collectionItem);

                    DeckCard deckCard = new DeckCard();
                    deckCard.setDeck(deck);
                    deckCard.setCollectionItem(collectionItem);
                    deckCard.setQuantity(request.quantity());
                    deckCard.setSection(request.section());

                    DeckCard savedDeckCard = deckCardRepository.save(deckCard);
                    return toDeckCardResponse(savedDeckCard);
                });
    }

    @Transactional(readOnly = true)
    public List<DeckCardResponse> getDeckCards(Long ownerId, Long deckId) {
        getDeckEntityByIdAndOwnerId(ownerId, deckId);

        return deckCardRepository.findByDeckId(deckId)
                .stream()
                .map(this::toDeckCardResponse)
                .toList();
    }

    @Transactional
    public void removeCardFromDeck(Long ownerId, Long deckId, Long deckCardId) {
        getDeckEntityByIdAndOwnerId(ownerId, deckId);

        DeckCard deckCard = deckCardRepository.findByIdAndDeckOwnerId(deckCardId, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Deck card not found with id: " + deckCardId
                ));

        if (!deckCard.getDeck().getId().equals(deckId)) {
            throw new BadRequestException("Deck card does not belong to this deck");
        }

        deckCardRepository.delete(deckCard);
    }

    @Transactional(readOnly = true)
    public Deck getDeckEntityByIdAndOwnerId(Long ownerId, Long deckId) {
        return deckRepository.findByIdAndOwnerId(deckId, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Deck not found with id: " + deckId
                ));
    }

    private void validateCommanderRules(Deck deck, DeckCardAddRequest request) {
        if (request.section() != DeckCardSection.COMMANDER) {
            return;
        }

        if (deck.getFormat() != DeckFormat.COMMANDER) {
            throw new BadRequestException("Only Commander decks can have a commander");
        }

        if (request.quantity() != 1) {
            throw new BadRequestException("Commander quantity must be one");
        }

        long commanders = deckCardRepository.countByDeckIdAndSection(deck.getId(), DeckCardSection.COMMANDER);

        boolean sameCommanderAlreadyExists = deckCardRepository
                .findByDeckIdAndCollectionItemIdAndSection(
                        deck.getId(),
                        request.collectionItemId(),
                        DeckCardSection.COMMANDER
                )
                .isPresent();

        if (commanders > 0 && !sameCommanderAlreadyExists) {
            throw new BadRequestException("A Commander deck can have only one commander");
        }
    }

    private void validateQuantityAgainstCollection(int requestedQuantity, CollectionItem collectionItem) {
        if (requestedQuantity > collectionItem.getQuantity()) {
            throw new BadRequestException("Deck quantity cannot exceed collection quantity");
        }
    }

    private DeckResponse toDeckResponse(Deck deck) {
        Long totalCards = deckCardRepository.countTotalCardsByDeckId(deck.getId());

        return new DeckResponse(
                deck.getId(),
                deck.getName(),
                deck.getDescription(),
                deck.getFormat(),
                deck.getOwner().getId(),
                totalCards,
                deck.getCreatedAt(),
                deck.getUpdatedAt()
        );
    }

    private DeckCardResponse toDeckCardResponse(DeckCard deckCard) {
        CollectionItem item = deckCard.getCollectionItem();

        return new DeckCardResponse(
                deckCard.getId(),
                item.getId(),
                item.getName(),
                item.getExpansion(),
                item.getManaCost(),
                item.getTypeLine(),
                item.getImageUrl(),
                deckCard.getQuantity(),
                deckCard.getSection()
        );
    }
}