package org.example.springboot_labb2.listener;

import jakarta.transaction.Transactional;
import org.example.springboot_labb2.entity.Message;
import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.repository.MessageRepository;
import org.example.springboot_labb2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class StartupApplicationListener {

    private static final Logger LOG = Logger.getLogger(StartupApplicationListener.class.getName());

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public StartupApplicationListener(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LOG.info("Application started!");
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOG.info("Application context has been refreshed.");
    }

    @Transactional
    public void saveUserAndMessage(User user, Message message) {
        if (user != null && message != null) {
            userRepository.save(user);
            messageRepository.save(message);
            LOG.info("User and message saved successfully.");
        } else {
            LOG.warning("User or message is null. Unable to save.");
        }
    }
}