package com.hospitality.platform.dto;

import com.hospitality.platform.entity.ChatMessage;
import java.time.LocalDateTime;

public class ChatMessageDTO {
    private Long id;
    private String senderRole;
    private String content;
    private LocalDateTime createdAt;

    public ChatMessageDTO() {}

    public ChatMessageDTO(ChatMessage message) {
        if (message != null) {
            this.id = message.getId();
            this.senderRole = message.getSenderRole();
            this.content = message.getContent();
            this.createdAt = message.getCreatedAt();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSenderRole() { return senderRole; }
    public void setSenderRole(String senderRole) { this.senderRole = senderRole; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
