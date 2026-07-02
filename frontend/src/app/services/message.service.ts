import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MessageCreateRequest } from '../models/message-create-request';
import { MessagePageResponse } from '../models/message-page-response';
import { MessageResponse } from '../models/message-response';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private readonly headers = new HttpHeaders({
    Authorization: 'Basic ' + btoa('admin:admin')
  });

  constructor(private http: HttpClient) {
  }

  sendMessage(senderId: number, request: MessageCreateRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(
      `/api/users/${senderId}/messages`,
      request,
      { headers: this.headers }
    );
  }

  getInbox(userId: number): Observable<MessagePageResponse> {
    return this.http.get<MessagePageResponse>(
      `/api/users/${userId}/messages/inbox`,
      { headers: this.headers }
    );
  }

  getSentMessages(userId: number): Observable<MessagePageResponse> {
    return this.http.get<MessagePageResponse>(
      `/api/users/${userId}/messages/sent`,
      { headers: this.headers }
    );
  }

  getConversation(userId: number, otherUserId: number): Observable<MessageResponse[]> {
    return this.http.get<MessageResponse[]>(
      `/api/users/${userId}/messages/conversation/${otherUserId}`,
      { headers: this.headers }
    );
  }

  markAsRead(userId: number, messageId: number): Observable<MessageResponse> {
    return this.http.patch<MessageResponse>(
      `/api/users/${userId}/messages/${messageId}/read`,
      {},
      { headers: this.headers }
    );
  }
}