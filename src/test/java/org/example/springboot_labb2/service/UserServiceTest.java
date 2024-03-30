package org.example.springboot_labb2.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.exception.ResourceNotFoundException;
import org.example.springboot_labb2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setRole("USER");
        // set other fields...
    }

    @Test
    void getAllUsers_ShouldReturnUserList() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<User> users = userService.getAllUsers();

        assertThat(users).isNotEmpty();
        assertThat(users.get(0)).isEqualTo(user);
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1L);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(user);
    }

    @Test
    void createUser_ShouldReturnNewUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User newUser = userService.createUser(new User());

        assertThat(newUser).isEqualTo(user);
    }

    @Test
    void updateUser_ExistingUser_ShouldReturnUpdatedUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);

        user.setEmail("newemail@example.com");
        User updatedUser = userService.updateUser(1L, user);

        assertThat(updatedUser.getEmail()).isEqualTo("newemail@example.com");
    }

    @Test
    void updateUser_NonExistingUser_ShouldThrowException() {
        Long nonExistingId = 2L;
        when(userRepository.existsById(nonExistingId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(nonExistingId, new User());
        });
    }

    @Test
    void deleteUser_ShouldInvokeDeletion() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}
