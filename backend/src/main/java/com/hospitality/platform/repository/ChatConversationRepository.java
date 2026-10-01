package com.hospitality.platform.repository;

import com.hospitality.platform.entity.ChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {
    Optional<ChatConversation> findByConversationId(String conversationId);
    List<ChatConversation> findByUserIdOrderByUpdatedAtDesc(Long userId);
}
