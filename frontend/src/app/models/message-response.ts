export interface MessageResponse {
  id: number;
  senderId: number;
  senderUsername: string;
  receiverId: number;
  receiverUsername: string;
  content: string;
  readMessage: boolean;
  sentAt: string;
  readAt: string;
}