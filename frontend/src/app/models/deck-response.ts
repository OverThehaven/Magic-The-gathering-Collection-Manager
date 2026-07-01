export interface DeckResponse {
  id: number;
  name: string;
  description: string;
  format: string;
  ownerId: number;
  totalCards: number;
  createdAt: string;
  updatedAt: string;
}