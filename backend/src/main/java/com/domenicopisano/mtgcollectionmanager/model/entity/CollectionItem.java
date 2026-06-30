package com.domenicopisano.mtgcollectionmanager.model.entity;

import com.domenicopisano.mtgcollectionmanager.model.enums.CardCondition;
import com.domenicopisano.mtgcollectionmanager.model.enums.CardLanguage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "collection_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_collection_card_variant",
                        columnNames = {"owner_id", "external_card_id", "card_language", "card_condition", "foil"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CollectionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // In futuro questo sarà l'id della carta ottenuto dal servizio esterno
    @Column(name = "external_card_id", nullable = false)
    private String externalCardId;

    @Column(nullable = false)
    private String name;

    private String expansion;

    private String rarity;

    private String manaCost;

    private String typeLine;

    @Column(length = 1000)
    private String imageUrl;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_language", nullable = false)
    private CardLanguage language;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_condition", nullable = false)
    private CardCondition condition;

    @Column(nullable = false)
    private boolean foil;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserProfile owner;
}