package com.testorystudio.testorystudio.controllers;

import com.testorystudio.testorystudio.Entities.WebsocketMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
@CrossOrigin(origins = "http://localhost:8081")
public class WebSocketController {

    private SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping(path = "/ws", produces = "application/text")
    public @ResponseBody String getSocketId() {
        System.out.println("GetSocktId");
        String socketId = UUID.randomUUID().toString();
        return socketId;
    }

    @MessageMapping("/createSocketId")
    @SendTo("/broker/socketId")
    public String connectNewClient(String testId) {
        String socketId = UUID.randomUUID().toString();
        return socketId;
    }

    @MessageMapping("/disconnectSocket")
    public void disconnectClient(String socketId) {
        // Currently not needed.
    }

    public void sendUpdate(String socketId, WebsocketMessage message) {
        final String endPoint = "/broker/" + socketId;
        this.messagingTemplate.convertAndSend(endPoint, message);
    }
}
