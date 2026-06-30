package com.domenicopisano.mtgcollectionmanager.model.entity;

import com.domenicopisano.mtgcollectionmanager.model.enums.DeckCardSection;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Getter
@Setter
@NoArgsConstructor
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
}