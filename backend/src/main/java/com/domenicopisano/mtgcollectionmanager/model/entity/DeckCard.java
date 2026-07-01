package com.domenicopisano.mtgcollectionmanager.model.entity;

import com.domenicopisano.mtgcollectionmanager.model.enums.DeckCardSection;
import jakarta.persistence.*;

@Entity
@Table(
        name = "deck_cards",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_deck_card_section",
                        columnNames = {"deck_id", "collection_item_id", "section"}
                )
        }
)
public class DeckCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeckCardSection section;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "collection_item_id", nullable = false)
    private CollectionItem collectionItem;

    public DeckCard() {
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public DeckCardSection getSection() {
        return section;
    }

    public void setSection(DeckCardSection section) {
        this.section = section;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public CollectionItem getCollectionItem() {
        return collectionItem;
    }

    public void setCollectionItem(CollectionItem collectionItem) {
        this.collectionItem = collectionItem;
    }
}