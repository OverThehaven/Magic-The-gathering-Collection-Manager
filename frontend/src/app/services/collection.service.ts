import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CollectionItemCreateRequest } from '../models/collection-item-create-request';
import { CollectionPageResponse } from '../models/collection-page-response';

@Injectable({
  providedIn: 'root'
})
export class CollectionService {

  private readonly headers = new HttpHeaders({
    Authorization: 'Basic ' + btoa('admin:admin')
  });

  constructor(private http: HttpClient) {
  }

  addCardToCollection(ownerId: number, request: CollectionItemCreateRequest): Observable<void> {
    return this.http.post<void>(
      `/api/users/${ownerId}/collection/simple`,
      request,
      { headers: this.headers }
    );
  }

  getCollection(ownerId: number): Observable<CollectionPageResponse> {
    return this.http.get<CollectionPageResponse>(
      `/api/users/${ownerId}/collection`,
      { headers: this.headers }
    );
  }

  removeCardFromCollection(ownerId: number, collectionItemId: number): Observable<void> {
    return this.http.delete<void>(
      `/api/users/${ownerId}/collection/${collectionItemId}`,
      { headers: this.headers }
    );
  }
}