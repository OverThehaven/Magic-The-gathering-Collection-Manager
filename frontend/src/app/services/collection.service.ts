import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CollectionItemCreateRequest } from '../models/collection-item-create-request';

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
}