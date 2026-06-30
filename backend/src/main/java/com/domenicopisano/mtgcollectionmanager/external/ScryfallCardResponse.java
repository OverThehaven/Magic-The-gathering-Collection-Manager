package com.domenicopisano.mtgcollectionmanager.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ScryfallCardResponse(
        String id,
        String name,

        @JsonProperty("set_name")
        String setName,

        String rarity,

        @JsonProperty("mana_cost")
        String manaCost,

        @JsonProperty("type_line")
        String typeLine,

        @JsonProperty("oracle_text")
        String oracleText,

        @JsonProperty("image_uris")
        ImageUris imageUris,

        @JsonProperty("card_faces")
        List<CardFace> cardFaces
) {

    public String bestImageUrl() {
        if (imageUris != null && imageUris.normal() != null) {
            return imageUris.normal();
        }

        if (cardFaces != null && !cardFaces.isEmpty()) {
            CardFace firstFace = cardFaces.get(0);
            if (firstFace.imageUris() != null && firstFace.imageUris().normal() != null) {
                return firstFace.imageUris().normal();
            }
        }

        return null;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ImageUris(
            String small,
            String normal,
            String large
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CardFace(
            String name,

            @JsonProperty("image_uris")
            ImageUris imageUris
    ) {
    }
}