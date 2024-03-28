package org.example.springboot_labb2.listener;

import org.example.springboot_labb2.entity.Message;
import org.example.springboot_labb2.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

@Component
public class StartupApplicationListener {

    private static final Logger LOG = Logger.getLogger(StartupApplicationListener.class.getName());

    private final MessageRepository messageRepository;

    @Autowired
    public StartupApplicationListener(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @EventListener
    @Transactional
    public void onApplicationReady(ApplicationReadyEvent event) {
        LOG.info("Checking for existing messages.");

        List<Message> messages = messageRepository.findAll();
        if (messages.isEmpty()) {
            LOG.info("No messages found. Creating a default message.");
            Message defaultMessage = new Message();
            defaultMessage.setTitle("Welcome to the jungle");
            defaultMessage.setContent("We've got fun and games.");
            messageRepository.save(defaultMessage);
            LOG.info("Default message saved.");
        } else {
            LOG.info("Messages found. Skipping creation of default message.");
        }
    }
}
