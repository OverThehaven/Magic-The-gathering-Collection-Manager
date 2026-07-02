import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserProfileResponse } from '../models/user-profile-response';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly headers = new HttpHeaders({
    Authorization: 'Basic ' + btoa('admin:admin')
  });

  constructor(private http: HttpClient) {
  }

  getUsers(): Observable<UserProfileResponse[]> {
    return this.http.get<UserProfileResponse[]>(
      '/api/users',
      { headers: this.headers }
    );
  }
}