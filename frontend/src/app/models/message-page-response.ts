import { MessageResponse } from './message-response';

export interface MessagePageResponse {
  content: MessageResponse[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  numberOfElements: number;
}