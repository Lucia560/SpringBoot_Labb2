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
        return userRepository.findById(id).map(dbUser ->{
            dbUser.setNameSurname(user.getNameSurname());
            dbUser.setEmail(user.getEmail());
            dbUser.setUsername(user.getUsername());
            dbUser.setProfilePicture(user.getProfilePicture());
            return userRepository.save(dbUser);

        }).orElseThrow(()-> new ResourceNotFoundException("User with id " + id + " not found"));
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
        user.setProfilePicture(updatedUserDetails.getProfilePicture());
        return userRepository.save(user);
    }


    public List<User> getPage(int p, int i) {
     return userRepository.findUserBy(p,i);
    }

    public User findByGithubLogin(String login) {
        return userRepository.findByGithubLogin(login).orElseThrow(()-> new ResourceNotFoundException("User with login " + login + " not found"));
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}