package com.domenicopisano.mtgcollectionmanager.service;

import com.domenicopisano.mtgcollectionmanager.dto.CollectionItemCreateRequest;
import com.domenicopisano.mtgcollectionmanager.dto.CollectionItemQuantityUpdateRequest;
import com.domenicopisano.mtgcollectionmanager.dto.CollectionItemResponse;
import com.domenicopisano.mtgcollectionmanager.exception.ResourceNotFoundException;
import com.domenicopisano.mtgcollectionmanager.model.entity.CollectionItem;
import com.domenicopisano.mtgcollectionmanager.model.entity.UserProfile;
import com.domenicopisano.mtgcollectionmanager.repository.CollectionItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CollectionItemService {

    private final CollectionItemRepository collectionItemRepository;
    private final UserProfileService userProfileService;

    public CollectionItemService(
            CollectionItemRepository collectionItemRepository,
            UserProfileService userProfileService
    ) {
        this.collectionItemRepository = collectionItemRepository;
        this.userProfileService = userProfileService;
    }

    @Transactional
    public CollectionItemResponse addItem(Long ownerId, CollectionItemCreateRequest request) {
        UserProfile owner = userProfileService.getUserEntityById(ownerId);

        return collectionItemRepository
                .findByOwnerIdAndExternalCardIdAndLanguageAndConditionAndFoil(
                        ownerId,
                        request.externalCardId(),
                        request.language(),
                        request.condition(),
                        request.foil()
                )
                .map(existingItem -> {
                    existingItem.setQuantity(existingItem.getQuantity() + request.quantity());
                    return toResponse(existingItem);
                })
                .orElseGet(() -> {
                    CollectionItem item = new CollectionItem();
                    item.setOwner(owner);
                    item.setExternalCardId(request.externalCardId());
                    item.setName(request.name());
                    item.setExpansion(request.expansion());
                    item.setRarity(request.rarity());
                    item.setManaCost(request.manaCost());
                    item.setTypeLine(request.typeLine());
                    item.setImageUrl(request.imageUrl());
                    item.setQuantity(request.quantity());
                    item.setLanguage(request.language());
                    item.setCondition(request.condition());
                    item.setFoil(request.foil());

                    CollectionItem savedItem = collectionItemRepository.save(item);
                    return toResponse(savedItem);
                });
    }

    @Transactional(readOnly = true)
    public Page<CollectionItemResponse> getCollection(
            Long ownerId,
            String name,
            String expansion,
            String rarity,
            Pageable pageable
    ) {
        userProfileService.getUserEntityById(ownerId);

        Page<CollectionItem> items;

        if (name != null && !name.isBlank()) {
            items = collectionItemRepository.findByOwnerIdAndNameContainingIgnoreCase(
                    ownerId,
                    name,
                    pageable
            );
        } else if (expansion != null && !expansion.isBlank()) {
            items = collectionItemRepository.findByOwnerIdAndExpansionContainingIgnoreCase(
                    ownerId,
                    expansion,
                    pageable
            );
        } else if (rarity != null && !rarity.isBlank()) {
            items = collectionItemRepository.findByOwnerIdAndRarityContainingIgnoreCase(
                    ownerId,
                    rarity,
                    pageable
            );
        } else {
            items = collectionItemRepository.findByOwnerId(ownerId, pageable);
        }

        return items.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public CollectionItemResponse getItemById(Long ownerId, Long itemId) {
        CollectionItem item = getItemEntityByIdAndOwnerId(ownerId, itemId);
        return toResponse(item);
    }

    @Transactional
    public CollectionItemResponse updateQuantity(
            Long ownerId,
            Long itemId,
            CollectionItemQuantityUpdateRequest request
    ) {
        CollectionItem item = getItemEntityByIdAndOwnerId(ownerId, itemId);
        item.setQuantity(request.quantity());

        return toResponse(item);
    }

    @Transactional
    public void deleteItem(Long ownerId, Long itemId) {
        CollectionItem item = getItemEntityByIdAndOwnerId(ownerId, itemId);
        collectionItemRepository.delete(item);
    }

    @Transactional(readOnly = true)
    public CollectionItem getItemEntityByIdAndOwnerId(Long ownerId, Long itemId) {
        return collectionItemRepository.findByIdAndOwnerId(itemId, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Collection item not found with id: " + itemId
                ));
    }

    private CollectionItemResponse toResponse(CollectionItem item) {
        return new CollectionItemResponse(
                item.getId(),
                item.getExternalCardId(),
                item.getName(),
                item.getExpansion(),
                item.getRarity(),
                item.getManaCost(),
                item.getTypeLine(),
                item.getImageUrl(),
                item.getQuantity(),
                item.getLanguage(),
                item.getCondition(),
                item.isFoil(),
                item.getOwner().getId()
        );
    }
}