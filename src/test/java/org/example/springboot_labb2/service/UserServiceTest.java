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

import java.util.Collections;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

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

        assertThat(foundUser).contains(user);
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
        Long inexistentId = 2L;
        when(userRepository.existsById(inexistentId)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> attemptUpdateUser(inexistentId));

        String expectedMessage = "User with id " + inexistentId + " not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    private void attemptUpdateUser(Long id) {
        userService.updateUser(id, new User());
    }

    @Test
    void deleteUser_ShouldInvokeDeletion() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }


    @Test
    void findByUsername_ExistingUser_ShouldReturnUser() {
        String existingUsername = "testUser";
        when(userRepository.findByUsername(existingUsername)).thenReturn(Collections.singletonList(user));

        User foundUser = userService.findByUsername(existingUsername);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(existingUsername);
    }

    @Test
    void findByUsername_NonExistingUser_ShouldThrowException() {
        String nonExistingUsername = "nonExistingUser";
        when(userRepository.findByUsername(nonExistingUsername)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.findByUsername(nonExistingUsername));

        String expectedMessage = "User with username " + nonExistingUsername + " not found";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void updateUserByUsername_ExistingUser_ShouldReturnUpdatedUser() {
        String oldUsername = "testUser";
        User originalUser = new User();
        originalUser.setId(1L);
        originalUser.setUsername(oldUsername);

        User updatedUserDetails = new User();
        updatedUserDetails.setUsername("newTestUser");
        updatedUserDetails.setNameSurname("New NameSurname");
        updatedUserDetails.setEmail("newemail@example.com");
        updatedUserDetails.setProfilePictureUrl("new-profile-pic-url");

        when(userRepository.findByUsername(oldUsername)).thenReturn(Collections.singletonList(originalUser));
        when(userRepository.findByUsername(updatedUserDetails.getUsername())).thenReturn(Collections.emptyList());
        when(userRepository.save(any(User.class))).thenReturn(originalUser);

        User updatedUser = userService.updateUserByUsername(oldUsername, updatedUserDetails);

        assertThat(updatedUser.getUsername()).isEqualTo(updatedUserDetails.getUsername());
        assertThat(updatedUser.getNameSurname()).isEqualTo(updatedUserDetails.getNameSurname());
        assertThat(updatedUser.getEmail()).isEqualTo(updatedUserDetails.getEmail());
        assertThat(updatedUser.getProfilePictureUrl()).isEqualTo(updatedUserDetails.getProfilePictureUrl());
    }

    @Test
    void updateUserByUsername_ExistingUserWithTakenNewUsername_ShouldThrowException() {
        String oldUsername = "oldUsername";
        String takenUsername = "takenUsername";
        User originalUser = new User();
        originalUser.setUsername(oldUsername);

        User updatedUserDetails = new User();
        updatedUserDetails.setUsername(takenUsername);

        when(userRepository.findByUsername(oldUsername)).thenReturn(Collections.singletonList(originalUser));
        when(userRepository.findByUsername(takenUsername)).thenReturn(Collections.singletonList(new User()));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> userService.updateUserByUsername(oldUsername, updatedUserDetails));

        assertThat(exception.getMessage()).contains("Username " + takenUsername + " is already taken");
    }
    @Test
    void updateUserByUsername_NonExistingUser_ShouldThrowResourceNotFoundException() {
        String nonExistingUsername = "nonExistingUsername";
        User updatedUserDetails = new User();
        updatedUserDetails.setUsername("newUsername");

        when(userRepository.findByUsername(nonExistingUsername)).thenReturn(Collections.emptyList());


        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUserByUsername(nonExistingUsername, updatedUserDetails));

        String expectedMessage = "User not found with username: " + nonExistingUsername;
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }





}
