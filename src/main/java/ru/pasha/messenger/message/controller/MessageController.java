package ru.pasha.messenger.message.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pasha.messenger.message.domain.Message;
import ru.pasha.messenger.message.requestEntity.RequestUpdateEntity;
import ru.pasha.messenger.message.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody Message message) {
        messageService.saveMessage(message);

        return new ResponseEntity<>("Message sent successfully", HttpStatus.OK);
    }

    @GetMapping("/chat/{id}")
    public ResponseEntity<List<Message>> getMessagesFromChat(@PathVariable Long id) {
        return new ResponseEntity<>(messageService.getMessagesFromChat(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        return new ResponseEntity<>(messageService.getMessageById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessageById(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return new ResponseEntity<>("Message deleted successfully", HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateMessageById(@PathVariable Long id, @RequestBody RequestUpdateEntity message) {
        messageService.updateMessageById(message, id);
        return new ResponseEntity<>("Message updated successfully", HttpStatus.OK);
    }
}
