package com.domenicopisano.mtgcollectionmanager.external;

import com.domenicopisano.mtgcollectionmanager.exception.BadRequestException;
import com.domenicopisano.mtgcollectionmanager.exception.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class ScryfallClient {

    private final RestClient restClient;

    public ScryfallClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("https://api.scryfall.com")
                .defaultHeader(HttpHeaders.USER_AGENT, "MTGCollectionManager/1.0")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

    public ScryfallCardResponse findCardByName(String name) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/cards/named")
                            .queryParam("fuzzy", name)
                            .build())
                    .retrieve()
                    .body(ScryfallCardResponse.class);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().value() == 404) {
                throw new ResourceNotFoundException("Card not found with name: " + name);
            }

            throw new BadRequestException("Error while searching card on external service");
        }
    }
}