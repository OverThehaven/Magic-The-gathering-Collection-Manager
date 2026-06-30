package com.domenicopisano.mtgcollectionmanager.repository;

import com.domenicopisano.mtgcollectionmanager.model.entity.CollectionItem;
import com.domenicopisano.mtgcollectionmanager.model.enums.CardCondition;
import com.domenicopisano.mtgcollectionmanager.model.enums.CardLanguage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionItemRepository extends JpaRepository<CollectionItem, Long> {

    Page<CollectionItem> findByOwnerId(Long ownerId, Pageable pageable);

    Optional<CollectionItem> findByIdAndOwnerId(Long id, Long ownerId);

    Optional<CollectionItem> findByOwnerIdAndExternalCardIdAndLanguageAndConditionAndFoil(
            Long ownerId,
            String externalCardId,
            CardLanguage language,
            CardCondition condition,
            boolean foil
    );

    Page<CollectionItem> findByOwnerIdAndNameContainingIgnoreCase(
            Long ownerId,
            String name,
            Pageable pageable
    );

    Page<CollectionItem> findByOwnerIdAndExpansionContainingIgnoreCase(
            Long ownerId,
            String expansion,
            Pageable pageable
    );

    Page<CollectionItem> findByOwnerIdAndRarityContainingIgnoreCase(
            Long ownerId,
            String rarity,
            Pageable pageable
    );
}