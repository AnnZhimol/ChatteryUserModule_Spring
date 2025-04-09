package com.example.chatteryusermodule.controllers;

import com.example.chatteryusermodule.dto.SentMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin("http://localhost:5173")
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastMessage(SentMessage message) {
        messagingTemplate.convertAndSend("/topic/translation/" + message.getTransId(), message);
    }
}
