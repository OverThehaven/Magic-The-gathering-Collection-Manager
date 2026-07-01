package com.domenicopisano.mtgcollectionmanager.dto;

import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        Long senderId,
        String senderUsername,
        Long receiverId,
        String receiverUsername,
        String content,
        boolean readMessage,
        LocalDateTime sentAt,
        LocalDateTime readAt
) {
}