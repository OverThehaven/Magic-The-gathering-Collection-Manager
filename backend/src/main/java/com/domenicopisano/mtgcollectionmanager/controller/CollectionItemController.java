package com.domenicopisano.mtgcollectionmanager.controller;

import com.domenicopisano.mtgcollectionmanager.dto.CollectionItemCreateRequest;
import com.domenicopisano.mtgcollectionmanager.dto.CollectionItemQuantityUpdateRequest;
import com.domenicopisano.mtgcollectionmanager.dto.CollectionItemResponse;
import com.domenicopisano.mtgcollectionmanager.service.CollectionItemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{ownerId}/collection")
public class CollectionItemController {

    private final CollectionItemService collectionItemService;

    public CollectionItemController(CollectionItemService collectionItemService) {
        this.collectionItemService = collectionItemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CollectionItemResponse addItem(
            @PathVariable Long ownerId,
            @Valid @RequestBody CollectionItemCreateRequest request
    ) {
        return collectionItemService.addItem(ownerId, request);
    }

    @PostMapping("/simple")
    @ResponseStatus(HttpStatus.CREATED)
    public void addItemSimple(
            @PathVariable Long ownerId,
            @Valid @RequestBody CollectionItemCreateRequest request
    ) {
        collectionItemService.addItem(ownerId, request);
    }

    @GetMapping
    public Page<CollectionItemResponse> getCollection(
            @PathVariable Long ownerId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String expansion,
            @RequestParam(required = false) String rarity,
            @PageableDefault(size = 10, sort = "name") Pageable pageable
    ) {
        return collectionItemService.getCollection(ownerId, name, expansion, rarity, pageable);
    }

    @GetMapping("/{itemId}")
    public CollectionItemResponse getItemById(
            @PathVariable Long ownerId,
            @PathVariable Long itemId
    ) {
        return collectionItemService.getItemById(ownerId, itemId);
    }

    @PatchMapping("/{itemId}/quantity")
    public CollectionItemResponse updateQuantity(
            @PathVariable Long ownerId,
            @PathVariable Long itemId,
            @Valid @RequestBody CollectionItemQuantityUpdateRequest request
    ) {
        return collectionItemService.updateQuantity(ownerId, itemId, request);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(
            @PathVariable Long ownerId,
            @PathVariable Long itemId
    ) {
        collectionItemService.deleteItem(ownerId, itemId);
    }
}