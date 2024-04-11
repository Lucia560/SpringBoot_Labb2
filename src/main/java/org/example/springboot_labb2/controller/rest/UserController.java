package org.example.springboot_labb2.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.springboot_labb2.exception.ResourceNotFoundException;
import org.example.springboot_labb2.repository.UserRepository;
import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.service.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "API endpoints för användarhantering")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Hämta alla användare")
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .peek(user -> user.setUsername(HtmlUtils.htmlEscape(user.getUsername())))
                .toList();
    }

    @GetMapping("/{id}")
    @Cacheable("user")
    @Operation(summary = "Hämta användare efter specifik ID")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "Användar-ID", required = true) @PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Skapa en ny användare")
    @CacheEvict(value = "allUsers", allEntries = true)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.created(URI.create("/api/users/" + savedUser.getId())).body(savedUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Uppdatera användare")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "Användar-ID", required = true) @PathVariable Long id, @RequestBody User user) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Ta bort användare")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Användar-ID", required = true) @PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{username}/edit")
    @Operation(summary = "Uppdatera användarprofil")
    public ResponseEntity<User> updateProfile(
            @Parameter(description = "Användarnamn", required = true) @PathVariable String username, @RequestBody User updatedUser) {
        try {
            User user = userService.updateUserByUsername(username, updatedUser);
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
