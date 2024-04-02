package org.example.springboot_labb2.controller;

import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.repository.UserRepository;
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
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void getAllUsers_ShouldReturnUserList() throws Exception {
        when(userRepository.findAll()).thenReturn(Arrays.asList(new User(), new User()));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.intValue())));
    }

    @Test
    public void getUserById_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createUser_ShouldCreateUserAndReturnCreatedUser() throws Exception {
        User user = new User();
        user.setUsername("newUser");
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    @Test
    public void updateUser_WhenUserExists_ShouldUpdateUserAndReturnIt() throws Exception {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("updatedUser");
        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(existingUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(existingUser.getUsername())));
    }

    @Test
    public void updateUser_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        Long userId = 1L;
        User updateUser = new User();
        updateUser.setUsername("nonExistentUser");
        when(userRepository.existsById(userId)).thenReturn(false);

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUser_WhenUserExists_ShouldDeleteUserAndReturnNoContent() throws Exception {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteUser_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNotFound());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}