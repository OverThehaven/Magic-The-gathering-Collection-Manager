import { CollectionItemResponse } from './collection-item-response';

export interface CollectionPageResponse {
  content: CollectionItemResponse[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  numberOfElements: number;
}