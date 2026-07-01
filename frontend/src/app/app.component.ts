import { ChangeDetectorRef, Component } from '@angular/core';
import { CardSearchResponse } from './models/card-search-response';
import { CollectionItemCreateRequest } from './models/collection-item-create-request';
import { CardService } from './services/card.service';
import { CollectionService } from './services/collection.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: false
})
export class AppComponent {

  readonly ownerId = 1;

  cardName = '';
  card?: CardSearchResponse;

  quantity = 1;
  language = 'ENGLISH';
  condition = 'NEAR_MINT';
  foil = false;

  errorMessage = '';
  successMessage = '';
  loading = false;
  addingToCollection = false;

  constructor(
    private cardService: CardService,
    private collectionService: CollectionService,
    private changeDetectorRef: ChangeDetectorRef
  ) {
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
        this.addingToCollection = false;
        this.successMessage = `${request.name} aggiunta alla collezione.`;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.addingToCollection = false;
        this.errorMessage = 'Errore durante l aggiunta alla collezione.';
        this.changeDetectorRef.detectChanges();
      }
    });

    setTimeout(() => {
      this.addingToCollection = false;
      this.successMessage = `${request.name} aggiunta alla collezione.`;
      this.changeDetectorRef.detectChanges();
    }, 1500);
  }
}