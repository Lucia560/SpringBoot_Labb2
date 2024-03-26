package org.example.springboot_labb2.service;

import org.example.springboot_labb2.exception.ResourceNotFoundException;
import org.example.springboot_labb2.entity.Message;
import org.example.springboot_labb2.repository.MessageRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message updateMessage(Long id, Message message) {
        if (messageRepository.existsById(id)) {
            message.setId(id);
            return messageRepository.save(message);
        }
        throw new ResourceNotFoundException("Message with id " + id + " not found");
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}