package com.domenicopisano.mtgcollectionmanager.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserProfile sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserProfile receiver;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private boolean readMessage = false;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    private LocalDateTime readAt;

    public Message() {
    }

    @PrePersist
    public void onCreate() {
        this.sentAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public UserProfile getSender() {
        return sender;
    }

    public void setSender(UserProfile sender) {
        this.sender = sender;
    }

    public UserProfile getReceiver() {
        return receiver;
    }

    public void setReceiver(UserProfile receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isReadMessage() {
        return readMessage;
    }

    public void setReadMessage(boolean readMessage) {
        this.readMessage = readMessage;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
}