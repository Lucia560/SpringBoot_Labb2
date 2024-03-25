package org.example.springboot_labb2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
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
}