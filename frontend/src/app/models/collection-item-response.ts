export interface CollectionItemResponse {
  id: number;
  externalCardId: string;
  name: string;
  expansion: string;
  rarity: string;
  manaCost: string;
  typeLine: string;
  imageUrl: string;
  quantity: number;
  language: string;
  condition: string;
  foil: boolean;
  ownerId: number;
}