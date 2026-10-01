package com.hospitality.platform.service;

import com.hospitality.platform.dto.ChatRequest;
import com.hospitality.platform.dto.ChatResponse;
import com.hospitality.platform.entity.Hotel;
import com.hospitality.platform.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AiChatService {

    @Autowired
    private HotelRepository hotelRepository;

    public ChatResponse processChat(ChatRequest request) {
        String message = request.getMessage() != null ? request.getMessage().toLowerCase() : "";
        String convId = request.getConversationId();
        if (convId == null || convId.trim().isEmpty()) {
            convId = UUID.randomUUID().toString();
        }

        String reply;

        if (message.contains("hotel") || message.contains("stay") || message.contains("recommend") || message.contains("place")) {
            List<Hotel> hotels = hotelRepository.findByActiveTrue();
            if (hotels.isEmpty()) {
                reply = "I currently don't see active hotels listed in our system, but check back soon!";
            } else {
                String hotelNames = hotels.stream().limit(3).map(Hotel::getName).collect(Collectors.joining(", "));
                reply = "I recommend checking out these top destinations on our platform: " + hotelNames + ". You can filter by city or rating on the main page!";
            }
        } else if (message.contains("book") || message.contains("reserve") || message.contains("cancel")) {
            reply = "To manage your bookings, select any hotel room, pick your dates, and proceed to instant checkout. You can review or cancel your reservations anytime under 'My Bookings'.";
        } else if (message.contains("price") || message.contains("cost") || message.contains("discount")) {
            reply = "Our luxury rooms start from competitive nightly rates! Browse individual hotel pages to see live pricing, discounts, and room inclusions.";
        } else if (message.contains("hello") || message.contains("hi") || message.contains("hey")) {
            reply = "Hello! 👋 I am your Hospitality AI Concierge. How can I help you find the perfect hotel stay today?";
        } else {
            reply = "Thanks for asking! I'm your AI Concierge. I can help you find hotels, check room availability, understand booking rules, or give travel suggestions. What destination are you interested in?";
        }

        return new ChatResponse(reply, convId);
    }
}
