package com.domenicopisano.mtgcollectionmanager.service;

import com.domenicopisano.mtgcollectionmanager.dto.MessageCreateRequest;
import com.domenicopisano.mtgcollectionmanager.dto.MessageResponse;
import com.domenicopisano.mtgcollectionmanager.exception.BadRequestException;
import com.domenicopisano.mtgcollectionmanager.exception.ResourceNotFoundException;
import com.domenicopisano.mtgcollectionmanager.model.entity.Message;
import com.domenicopisano.mtgcollectionmanager.model.entity.UserProfile;
import com.domenicopisano.mtgcollectionmanager.repository.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserProfileService userProfileService;

    public MessageService(MessageRepository messageRepository, UserProfileService userProfileService) {
        this.messageRepository = messageRepository;
        this.userProfileService = userProfileService;
    }

    @Transactional
    public MessageResponse sendMessage(Long senderId, MessageCreateRequest request) {
        if (senderId.equals(request.receiverId())) {
            throw new BadRequestException("Sender and receiver cannot be the same user");
        }

        UserProfile sender = userProfileService.getUserEntityById(senderId);
        UserProfile receiver = userProfileService.getUserEntityById(request.receiverId());

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(request.content());
        message.setReadMessage(false);

        Message savedMessage = messageRepository.save(message);

        return toResponse(savedMessage);
    }

    @Transactional(readOnly = true)
    public Page<MessageResponse> getInbox(Long userId, Pageable pageable) {
        userProfileService.getUserEntityById(userId);

        return messageRepository.findByReceiverIdOrderBySentAtDesc(userId, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<MessageResponse> getSentMessages(Long userId, Pageable pageable) {
        userProfileService.getUserEntityById(userId);

        return messageRepository.findBySenderIdOrderBySentAtDesc(userId, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getConversation(Long userId, Long otherUserId) {
        userProfileService.getUserEntityById(userId);
        userProfileService.getUserEntityById(otherUserId);

        return messageRepository.findConversation(userId, otherUserId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public MessageResponse markAsRead(Long userId, Long messageId) {
        userProfileService.getUserEntityById(userId);

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + messageId));

        if (!message.getReceiver().getId().equals(userId)) {
            throw new BadRequestException("Only the receiver can mark this message as read");
        }

        if (!message.isReadMessage()) {
            message.setReadMessage(true);
            message.setReadAt(LocalDateTime.now());
        }

        return toResponse(message);
    }

    private MessageResponse toResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getReceiver().getId(),
                message.getReceiver().getUsername(),
                message.getContent(),
                message.isReadMessage(),
                message.getSentAt(),
                message.getReadAt()
        );
    }
}