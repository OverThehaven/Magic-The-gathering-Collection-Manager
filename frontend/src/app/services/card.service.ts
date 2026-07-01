import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CardSearchResponse } from '../models/card-search-response';

@Injectable({
  providedIn: 'root'
})
export class CardService {

  private readonly apiUrl = '/api/cards';

  private readonly headers = new HttpHeaders({
    Authorization: 'Basic ' + btoa('admin:admin')
  });

  constructor(private http: HttpClient) {
  }

  searchCardByName(name: string): Observable<CardSearchResponse> {
    return this.http.get<CardSearchResponse>(
      `${this.apiUrl}/search?name=${encodeURIComponent(name)}`,
      { headers: this.headers }
    );
  }
}