import { Component } from '@angular/core';
import { CardSearchResponse } from './models/card-search-response';
import { CardService } from './services/card.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: false
})
export class AppComponent {

  cardName = '';
  card?: CardSearchResponse;
  errorMessage = '';
  loading = false;

  constructor(private cardService: CardService) {
  }

  searchCard(): void {
    this.errorMessage = '';
    this.card = undefined;

    if (!this.cardName.trim()) {
      this.errorMessage = 'Inserisci il nome di una carta.';
      return;
    }

    this.loading = true;

    this.cardService.searchCardByName(this.cardName).subscribe({
      next: response => {
        this.card = response;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Carta non trovata o errore durante la ricerca.';
        this.loading = false;
      }
    });
  }
}