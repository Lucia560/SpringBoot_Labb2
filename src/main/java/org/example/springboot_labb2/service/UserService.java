package org.example.springboot_labb2.service;

import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.exception.ResourceNotFoundException;
import org.example.springboot_labb2.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Cacheable("allUsers")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            return userRepository.save(user);
        }
        throw new ResourceNotFoundException("User with id " + id + " not found");
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        List<User> users = userRepository.findByUsername(username);
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            throw new ResourceNotFoundException("User with username " + username + " not found");
        }
    }


    public User updateUserByUsername(String oldUsername, User updatedUserDetails) {
        List<User> users = userRepository.findByUsername(oldUsername);
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("User not found with username: " + oldUsername);
        }
        User user = users.get(0);

        if (!user.getUsername().equals(updatedUserDetails.getUsername())) {
            List<User> userWithNewUsername = userRepository.findByUsername(updatedUserDetails.getUsername());
            if (!userWithNewUsername.isEmpty()) {
                throw new IllegalArgumentException("Username " + updatedUserDetails.getUsername() + " is already taken");
            }
        }

        user.setUsername(updatedUserDetails.getUsername());
        user.setNameSurname(updatedUserDetails.getNameSurname());
        user.setEmail(updatedUserDetails.getEmail());
        user.setProfilePictureUrl(updatedUserDetails.getProfilePictureUrl());
        return userRepository.save(user);
    }



}