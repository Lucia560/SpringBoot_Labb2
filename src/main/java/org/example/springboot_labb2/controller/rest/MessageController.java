package org.example.springboot_labb2.controller.rest;

import org.example.springboot_labb2.repository.MessageRepository;
import org.example.springboot_labb2.entity.Message;
import org.example.springboot_labb2.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageRepository messageRepository;
    private final MessageService messageService;

    public MessageController(MessageRepository messageRepository, MessageService messageService) {
        this.messageRepository = messageRepository;
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();

        if (isAuthenticated) {
            List<Message> messages = messageRepository.findAll();
            return ResponseEntity.ok(messages);
        } else {

            List<Message> publicMessages = messageRepository.findAllByStatusPrivateIsFalse();
            return ResponseEntity.ok(publicMessages);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Optional<Message> messageOptional = messageRepository.findById(id);
        return messageOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message savedMessage = messageRepository.save(message);
        return ResponseEntity.created(URI.create("/api/messages/" + savedMessage.getId())).body(savedMessage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable Long id, @RequestBody Message message) {
        if (!messageRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        message.setId(id);
        messageRepository.save(message);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        if (!messageRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        messageRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/messages/{id}/privacy")
    public ResponseEntity<Message> updateMessagePrivacy(@PathVariable Long id, @RequestParam boolean isPrivate) {
        Message updatedMessage = messageService.updateMessageStatus(id, isPrivate);
        return ResponseEntity.ok(updatedMessage);
    }


}