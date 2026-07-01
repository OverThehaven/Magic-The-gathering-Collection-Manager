package com.domenicopisano.mtgcollectionmanager.repository;

import com.domenicopisano.mtgcollectionmanager.model.entity.DeckCard;
import com.domenicopisano.mtgcollectionmanager.model.enums.DeckCardSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeckCardRepository extends JpaRepository<DeckCard, Long> {

    List<DeckCard> findByDeckId(Long deckId);

    Optional<DeckCard> findByIdAndDeckOwnerId(Long id, Long ownerId);

    Optional<DeckCard> findByDeckIdAndCollectionItemIdAndSection(
            Long deckId,
            Long collectionItemId,
            DeckCardSection section
    );

    long countByDeckIdAndSection(Long deckId, DeckCardSection section);

    @Query("select coalesce(sum(dc.quantity), 0) from DeckCard dc where dc.deck.id = :deckId")
    Long countTotalCardsByDeckId(@Param("deckId") Long deckId);
}