package com.domenicopisano.mtgcollectionmanager.model.entity;

import com.domenicopisano.mtgcollectionmanager.model.enums.CardCondition;
import com.domenicopisano.mtgcollectionmanager.model.enums.CardLanguage;
import jakarta.persistence.*;

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
public class CollectionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public CollectionItem() {
    }

    public Long getId() {
        return id;
    }

    public String getExternalCardId() {
        return externalCardId;
    }

    public void setExternalCardId(String externalCardId) {
        this.externalCardId = externalCardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpansion() {
        return expansion;
    }

    public void setExpansion(String expansion) {
        this.expansion = expansion;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getManaCost() {
        return manaCost;
    }

    public void setManaCost(String manaCost) {
        this.manaCost = manaCost;
    }

    public String getTypeLine() {
        return typeLine;
    }

    public void setTypeLine(String typeLine) {
        this.typeLine = typeLine;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CardLanguage getLanguage() {
        return language;
    }

    public void setLanguage(CardLanguage language) {
        this.language = language;
    }

    public CardCondition getCondition() {
        return condition;
    }

    public void setCondition(CardCondition condition) {
        this.condition = condition;
    }

    public boolean isFoil() {
        return foil;
    }

    public void setFoil(boolean foil) {
        this.foil = foil;
    }

    public UserProfile getOwner() {
        return owner;
    }

    public void setOwner(UserProfile owner) {
        this.owner = owner;
    }
}