import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CardSearchResponse } from './models/card-search-response';
import { CollectionItemCreateRequest } from './models/collection-item-create-request';
import { CollectionItemResponse } from './models/collection-item-response';
import { DeckCardAddRequest } from './models/deck-card-add-request';
import { DeckCardResponse } from './models/deck-card-response';
import { DeckCreateRequest } from './models/deck-create-request';
import { DeckResponse } from './models/deck-response';
import { CardService } from './services/card.service';
import { CollectionService } from './services/collection.service';
import { DeckService } from './services/deck.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: false
})
export class AppComponent implements OnInit {

  readonly ownerId = 1;

  cardName = '';
  card?: CardSearchResponse;

  quantity = 1;
  language = 'ENGLISH';
  condition = 'NEAR_MINT';
  foil = false;

  collectionItems: CollectionItemResponse[] = [];
  collectionTotalElements = 0;

  deckName = '';
  deckDescription = '';
  deckFormat = 'COMMANDER';
  decks: DeckResponse[] = [];
  selectedDeckId?: number;
  selectedDeckCards: DeckCardResponse[] = [];

  selectedCollectionItemId?: number;
  deckCardQuantity = 1;
  deckCardSection = 'MAIN_DECK';

  errorMessage = '';
  successMessage = '';

  loading = false;
  addingToCollection = false;
  collectionLoading = false;
  deckLoading = false;
  creatingDeck = false;
  addingCardToDeck = false;

  constructor(
    private cardService: CardService,
    private collectionService: CollectionService,
    private deckService: DeckService,
    private changeDetectorRef: ChangeDetectorRef
  ) {
  }

  ngOnInit(): void {
    this.loadCollection();
    this.loadDecks();
  }

  searchCard(): void {
    this.errorMessage = '';
    this.successMessage = '';
    this.card = undefined;

    if (!this.cardName.trim()) {
      this.errorMessage = 'Inserisci il nome di una carta.';
      this.changeDetectorRef.detectChanges();
      return;
    }

    this.loading = true;
    this.changeDetectorRef.detectChanges();

    this.cardService.searchCardByName(this.cardName).subscribe({
      next: response => {
        this.card = response;
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Carta non trovata o errore durante la ricerca.';
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  addToCollection(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.card) {
      this.errorMessage = 'Cerca prima una carta.';
      this.changeDetectorRef.detectChanges();
      return;
    }

    if (this.quantity <= 0) {
      this.errorMessage = 'La quantità deve essere maggiore di zero.';
      this.changeDetectorRef.detectChanges();
      return;
    }

    const request: CollectionItemCreateRequest = {
      externalCardId: this.card.externalCardId,
      name: this.card.name,
      expansion: this.card.expansion,
      rarity: this.card.rarity,
      manaCost: this.card.manaCost,
      typeLine: this.card.typeLine,
      imageUrl: this.card.imageUrl,
      quantity: this.quantity,
      language: this.language,
      condition: this.condition,
      foil: this.foil
    };

    this.addingToCollection = true;
    this.changeDetectorRef.detectChanges();

    this.collectionService.addCardToCollection(this.ownerId, request).subscribe({
      next: () => {
      },
      error: () => {
        console.log('Richiesta di aggiunta collezione terminata lato browser.');
      }
    });

    setTimeout(() => {
      this.addingToCollection = false;
      this.successMessage = `${request.name} aggiunta alla collezione.`;
      this.loadCollection();
      this.changeDetectorRef.detectChanges();
    }, 1500);
  }

  loadCollection(): void {
    this.collectionLoading = true;
    this.changeDetectorRef.detectChanges();

    this.collectionService.getCollection(this.ownerId).subscribe({
      next: response => {
        this.collectionItems = response.content;
        this.collectionTotalElements = response.totalElements;
        this.collectionLoading = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.collectionLoading = false;
        this.errorMessage = 'Errore durante il caricamento della collezione.';
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  removeFromCollection(item: CollectionItemResponse): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.collectionService.removeCardFromCollection(this.ownerId, item.id).subscribe({
      next: () => {
        this.successMessage = `${item.name} rimossa dalla collezione.`;
        this.loadCollection();
        if (this.selectedDeckId) {
          this.loadDeckCards(this.selectedDeckId);
        }
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Impossibile rimuovere la carta dalla collezione. Se è presente in un mazzo, rimuovila prima dal mazzo.';
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  createDeck(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.deckName.trim()) {
      this.errorMessage = 'Inserisci il nome del mazzo.';
      this.changeDetectorRef.detectChanges();
      return;
    }

    const request: DeckCreateRequest = {
      name: this.deckName,
      description: this.deckDescription,
      format: this.deckFormat
    };

    this.creatingDeck = true;
    this.changeDetectorRef.detectChanges();

    this.deckService.createDeck(this.ownerId, request).subscribe({
      next: response => {
        this.creatingDeck = false;
        this.successMessage = `Mazzo ${response.name} creato.`;
        this.deckName = '';
        this.deckDescription = '';
        this.deckFormat = 'COMMANDER';
        this.loadDecks();
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.creatingDeck = false;
        this.errorMessage = 'Errore durante la creazione del mazzo.';
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  loadDecks(): void {
    this.deckLoading = true;
    this.changeDetectorRef.detectChanges();

    this.deckService.getDecks(this.ownerId).subscribe({
      next: response => {
        this.decks = response.content;
        this.deckLoading = false;

        if (!this.selectedDeckId && this.decks.length > 0) {
          this.selectedDeckId = this.decks[0].id;
          this.loadDeckCards(this.selectedDeckId);
        }

        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.deckLoading = false;
        this.errorMessage = 'Errore durante il caricamento dei mazzi.';
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  onDeckSelectionChange(): void {
    if (this.selectedDeckId) {
      this.loadDeckCards(this.selectedDeckId);
    } else {
      this.selectedDeckCards = [];
      this.changeDetectorRef.detectChanges();
    }
  }

  loadDeckCards(deckId: number): void {
    this.deckService.getDeckCards(this.ownerId, deckId).subscribe({
      next: response => {
        this.selectedDeckCards = response;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Errore durante il caricamento delle carte del mazzo.';
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  addCardToDeck(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.selectedDeckId) {
      this.errorMessage = 'Seleziona un mazzo.';
      this.changeDetectorRef.detectChanges();
      return;
    }

    if (!this.selectedCollectionItemId) {
      this.errorMessage = 'Seleziona una carta dalla collezione.';
      this.changeDetectorRef.detectChanges();
      return;
    }

    if (this.deckCardQuantity <= 0) {
      this.errorMessage = 'La quantità deve essere maggiore di zero.';
      this.changeDetectorRef.detectChanges();
      return;
    }

    const selectedItem = this.collectionItems.find(item => item.id === this.selectedCollectionItemId);

    const request: DeckCardAddRequest = {
      collectionItemId: this.selectedCollectionItemId,
      quantity: this.deckCardQuantity,
      section: this.deckCardSection
    };

    this.addingCardToDeck = true;
    this.changeDetectorRef.detectChanges();

    this.deckService.addCardToDeck(this.ownerId, this.selectedDeckId, request).subscribe({
      next: () => {
        this.addingCardToDeck = false;
        this.successMessage = `${selectedItem?.name ?? 'Carta'} aggiunta al mazzo.`;
        this.loadDeckCards(this.selectedDeckId!);
        this.loadDecks();
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.addingCardToDeck = false;
        this.errorMessage = 'Errore durante l aggiunta della carta al mazzo. Controlla quantità e sezione.';
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  removeDeckCard(deckCard: DeckCardResponse): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.selectedDeckId) {
      this.errorMessage = 'Seleziona un mazzo.';
      this.changeDetectorRef.detectChanges();
      return;
    }

    this.deckService.removeCardFromDeck(this.ownerId, this.selectedDeckId, deckCard.id).subscribe({
      next: () => {
        this.successMessage = `${deckCard.cardName} rimossa dal mazzo.`;
        this.loadDeckCards(this.selectedDeckId!);
        this.loadDecks();
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Errore durante la rimozione della carta dal mazzo.';
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  deleteSelectedDeck(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.selectedDeckId) {
      this.errorMessage = 'Seleziona un mazzo.';
      this.changeDetectorRef.detectChanges();
      return;
    }

    const deckToDelete = this.decks.find(deck => deck.id === this.selectedDeckId);

    this.deckService.deleteDeck(this.ownerId, this.selectedDeckId).subscribe({
      next: () => {
        this.successMessage = `Mazzo ${deckToDelete?.name ?? ''} eliminato.`;
        this.selectedDeckId = undefined;
        this.selectedDeckCards = [];
        this.loadDecks();
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Errore durante l eliminazione del mazzo.';
        this.changeDetectorRef.detectChanges();
      }
    });
  }
}