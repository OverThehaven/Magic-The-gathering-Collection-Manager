import { DeckResponse } from './deck-response';

export interface DeckPageResponse {
  content: DeckResponse[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  numberOfElements: number;
}