package org.example.springboot_labb2.service;

import org.example.springboot_labb2.entity.Message;
import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.exception.ResourceNotFoundException;
import org.example.springboot_labb2.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllMessages() {
        when(messageRepository.findAll()).thenReturn(Arrays.asList(new Message(), new Message()));

        List<Message> result = messageService.getAllMessages();

        assertThat(result).hasSize(2);
    }

    @Test
    void testFindMessageById() {
        Message message = new Message();
        message.setId(1L);
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        Optional<Message> result = messageService.getMessageById(1L);

        assertThat(result).isPresent().containsSame(message);
    }

    @Test
    void testCreateMessage() {
        Message message = new Message();
        User user = new User(); // Create a User object
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message result = messageService.createMessage(message, user); // Pass the User object

        assertThat(result).isSameAs(message);
    }


    @Test
    void testUpdateMessage() {
        Message existingMessage = new Message();
        Long id = 1L;
        User user = new User(); // Create a User object
        when(messageRepository.save(any(Message.class))).thenReturn(existingMessage);
        when(messageRepository.existsById(id)).thenReturn(true);

        Message updatedMessage = messageService.updateMessage(id, existingMessage, user); // Pass the User object

        assertThat(updatedMessage).isNotNull();
    }


    @Test
    void testDeleteMessage() {
        doNothing().when(messageRepository).deleteById(1L);

        messageService.deleteMessage(1L);

        verify(messageRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateMessageStatus() {
        Long messageId = 1L;
        Message message = new Message();
        message.setId(messageId);
        message.setStatusPrivate(false);
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message result = messageService.updateMessageStatus(messageId, true);

        assertThat(result.isStatusPrivate()).isTrue();
        verify(messageRepository).save(message);
    }

    @Test
    void testUpdateMessageStatusWhenMessageNotFound() {
        // Arrange
        Long messageId = 1L;
        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            messageService.updateMessageStatus(messageId, true);
        });
    }



}
