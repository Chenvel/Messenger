package ru.pasha.messenger.chat.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pasha.messenger.chat.domain.Chat;
import ru.pasha.messenger.chat.service.ChatService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<String> addChat(@RequestBody Chat chat) {
        chatService.saveChat(chat);
        return new ResponseEntity<>("Chat added successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Chat>> getAllChats() {
        return new ResponseEntity<>(chatService.getAllChats(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chat> getChatById(@PathVariable Long id) {
        return new ResponseEntity<>(chatService.getChatById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChatById(@PathVariable Long id) {
        chatService.deleteChatById(id);
        return new ResponseEntity<>("Chat deleted successfully", HttpStatus.OK);
    }
}
