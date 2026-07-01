import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DeckCardAddRequest } from '../models/deck-card-add-request';
import { DeckCardResponse } from '../models/deck-card-response';
import { DeckCreateRequest } from '../models/deck-create-request';
import { DeckPageResponse } from '../models/deck-page-response';
import { DeckResponse } from '../models/deck-response';

@Injectable({
  providedIn: 'root'
})
export class DeckService {

  private readonly headers = new HttpHeaders({
    Authorization: 'Basic ' + btoa('admin:admin')
  });

  constructor(private http: HttpClient) {
  }

  createDeck(ownerId: number, request: DeckCreateRequest): Observable<DeckResponse> {
    return this.http.post<DeckResponse>(
      `/api/users/${ownerId}/decks`,
      request,
      { headers: this.headers }
    );
  }

  getDecks(ownerId: number): Observable<DeckPageResponse> {
    return this.http.get<DeckPageResponse>(
      `/api/users/${ownerId}/decks`,
      { headers: this.headers }
    );
  }

  deleteDeck(ownerId: number, deckId: number): Observable<void> {
    return this.http.delete<void>(
      `/api/users/${ownerId}/decks/${deckId}`,
      { headers: this.headers }
    );
  }

  addCardToDeck(ownerId: number, deckId: number, request: DeckCardAddRequest): Observable<DeckCardResponse> {
    return this.http.post<DeckCardResponse>(
      `/api/users/${ownerId}/decks/${deckId}/cards`,
      request,
      { headers: this.headers }
    );
  }

  getDeckCards(ownerId: number, deckId: number): Observable<DeckCardResponse[]> {
    return this.http.get<DeckCardResponse[]>(
      `/api/users/${ownerId}/decks/${deckId}/cards`,
      { headers: this.headers }
    );
  }

  removeCardFromDeck(ownerId: number, deckId: number, deckCardId: number): Observable<void> {
    return this.http.delete<void>(
      `/api/users/${ownerId}/decks/${deckId}/cards/${deckCardId}`,
      { headers: this.headers }
    );
  }
}