package com.domenicopisano.mtgcollectionmanager.repository;

import com.domenicopisano.mtgcollectionmanager.model.entity.Deck;
import com.domenicopisano.mtgcollectionmanager.model.enums.DeckFormat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeckRepository extends JpaRepository<Deck, Long> {

    Page<Deck> findByOwnerId(Long ownerId, Pageable pageable);

    Optional<Deck> findByIdAndOwnerId(Long id, Long ownerId);

    Page<Deck> findByOwnerIdAndNameContainingIgnoreCase(
            Long ownerId,
            String name,
            Pageable pageable
    );

    Page<Deck> findByOwnerIdAndFormat(
            Long ownerId,
            DeckFormat format,
            Pageable pageable
    );
}