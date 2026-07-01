package com.domenicopisano.mtgcollectionmanager.controller;

import com.domenicopisano.mtgcollectionmanager.dto.MessageCreateRequest;
import com.domenicopisano.mtgcollectionmanager.dto.MessageResponse;
import com.domenicopisano.mtgcollectionmanager.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse sendMessage(
            @PathVariable Long userId,
            @Valid @RequestBody MessageCreateRequest request
    ) {
        return messageService.sendMessage(userId, request);
    }

    @GetMapping("/inbox")
    public Page<MessageResponse> getInbox(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "sentAt") Pageable pageable
    ) {
        return messageService.getInbox(userId, pageable);
    }

    @GetMapping("/sent")
    public Page<MessageResponse> getSentMessages(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "sentAt") Pageable pageable
    ) {
        return messageService.getSentMessages(userId, pageable);
    }

    @GetMapping("/conversation/{otherUserId}")
    public List<MessageResponse> getConversation(
            @PathVariable Long userId,
            @PathVariable Long otherUserId
    ) {
        return messageService.getConversation(userId, otherUserId);
    }

    @PatchMapping("/{messageId}/read")
    public MessageResponse markAsRead(
            @PathVariable Long userId,
            @PathVariable Long messageId
    ) {
        return messageService.markAsRead(userId, messageId);
    }
}