package com.hospitality.platform.controller;

import com.hospitality.platform.dto.ChatRequest;
import com.hospitality.platform.dto.ChatResponse;
import com.hospitality.platform.service.AiChatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private AiChatService aiChatService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        ChatResponse response = aiChatService.processChat(request);
        return ResponseEntity.ok(response);
    }
}
