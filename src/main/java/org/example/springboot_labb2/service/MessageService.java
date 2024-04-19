package org.example.springboot_labb2.service;

import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.exception.ResourceNotFoundException;
import org.example.springboot_labb2.entity.Message;
import org.example.springboot_labb2.repository.MessageRepository;
import org.example.springboot_labb2.repository.UserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    //private final TranslateService translateService;

    public MessageService(MessageRepository messageRepository, UserService userService ) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        //this.translateService = translateService;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getPublicMessages(){
        return messageRepository.findAllByStatusPrivateIsFalse();
    }

    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    /*public Message createMessage.html(Message message) {
        return messageRepository.save(message);
    }*/

    @Transactional
    public Message createMessage(Message message, OAuth2User principal) {
        User user = userService.findByGithubLogin(principal.getAttribute("login"));
        message.setUser(user); // Set the user
        return messageRepository.save(message);
    }

    public Message updateMessage(Long id, Message message,User user) {
        if (messageRepository.existsById(id)) {
            message.setId(id);
            return messageRepository.save(message);
        }
        throw new ResourceNotFoundException("Message with id " + id + " not found");
    }

    public Message updateMessageStatus(Long messageId, boolean isPrivate) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id " + messageId));
        message.setStatusPrivate(isPrivate);
        return messageRepository.save(message);
    }

}
