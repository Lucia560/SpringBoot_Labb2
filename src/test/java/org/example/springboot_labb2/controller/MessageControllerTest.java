package org.example.springboot_labb2.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


import static org.mockito.ArgumentMatchers.any;
import org.example.springboot_labb2.entity.Message;
import org.example.springboot_labb2.exception.ResourceNotFoundException;
import org.example.springboot_labb2.repository.MessageRepository;
import org.example.springboot_labb2.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {

    @InjectMocks
    private MessageService messageService;
    private MockMvc mockMvc;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageController messageController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController)
                .build();
    }

    @Test
    public void getAllMessages_ShouldReturnMessageList() throws Exception {
        when(messageRepository.findAll()).thenReturn(Arrays.asList(new Message(), new Message()));

        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getMessageById_WhenMessageExists_ShouldReturnMessage() throws Exception {
        Long messageId = 1L;
        Message message = new Message();
        message.setId(messageId);
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        mockMvc.perform(get("/api/messages/{id}", messageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(messageId.intValue())));
    }

    @Test
    public void getMessageById_WhenMessageDoesNotExist_ShouldReturnNotFound() throws Exception {
        Long messageId = 1L;
        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/messages/{id}", messageId))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateMessage_NonExistingId_ShouldThrowException() {
        Long nonExistingId = 99L;
        Message updateInfo = new Message();
        updateInfo.setTitle("Updated Title");
        updateInfo.setContent("Updated content");
        when(messageRepository.existsById(nonExistingId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            messageService.updateMessage(nonExistingId, updateInfo);
        });
    }

    @Test
    void deleteMessage_NonExistingId_ShouldReturnNotFound() throws Exception {
        Long nonExistingId = 99L;
        when(messageRepository.existsById(nonExistingId)).thenReturn(false);

        mockMvc.perform(delete("/api/messages/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }


    @Test
    public void whenCreateMessage_thenStatus201() throws Exception {
        Message newMessage = new Message();
        newMessage.setTitle("New Title");
        newMessage.setContent("New Content");

        when(messageService.createMessage(any(Message.class))).thenReturn(newMessage);

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newMessage)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.title", is(newMessage.getTitle())))
                .andExpect(jsonPath("$.content", is(newMessage.getContent())));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void givenMessageToUpdate_whenMessageExists_thenStatus200() throws Exception {
        Long existingId = 1L;
        Message existingMessage = new Message();
        existingMessage.setId(existingId);
        existingMessage.setTitle("Existing Title");
        existingMessage.setContent("Existing content");

        when(messageRepository.existsById(existingId)).thenReturn(true);
        when(messageRepository.save(any(Message.class))).thenReturn(existingMessage);

        mockMvc.perform(put("/api/messages/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(existingMessage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(existingId.intValue())))
                .andExpect(jsonPath("$.title", is(existingMessage.getTitle())))
                .andExpect(jsonPath("$.content", is(existingMessage.getContent())));
    }

    @Test
    public void givenMessageToUpdate_whenMessageDoesNotExist_thenStatus404() throws Exception {
        Long nonExistingId = 2L;
        Message updatedMessage = new Message();
        updatedMessage.setTitle("Updated Title");
        updatedMessage.setContent("Updated Content");

        when(messageRepository.existsById(nonExistingId)).thenReturn(false);

        mockMvc.perform(put("/api/messages/{id}", nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedMessage)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenMessageIdToDelete_whenMessageExists_thenStatus204() throws Exception {
        Long existingId = 1L;

        when(messageRepository.existsById(existingId)).thenReturn(true);
        doNothing().when(messageRepository).deleteById(existingId);

        mockMvc.perform(delete("/api/messages/{id}", existingId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenMessageIdToDelete_whenMessageDoesNotExist_thenStatus404() throws Exception {
        Long nonExistingId = 2L;

        when(messageRepository.existsById(nonExistingId)).thenReturn(false);

        mockMvc.perform(delete("/api/messages/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }


}