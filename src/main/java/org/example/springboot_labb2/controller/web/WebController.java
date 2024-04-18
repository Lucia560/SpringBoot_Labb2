package org.example.springboot_labb2.controller.web;

import org.example.springboot_labb2.entity.Message;
import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.repository.MessageRepository;
import org.example.springboot_labb2.service.MessageService;
import org.example.springboot_labb2.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/web")
public class WebController {

    private final UserService userService;
    private final MessageService messageService;
    private final MessageRepository messageRepository;

    public WebController(UserService userService, MessageService messageService, MessageRepository messageRepository) {
        this.userService = userService;
        this.messageService = messageService;
        this.messageRepository = messageRepository;
    }

    @GetMapping("users")
    public String getUsers(Model model) {
        var users = userService.getPage(0, 10);
        model.addAttribute("nextpage", users.getLast().getId());
        model.addAttribute("usersNames", users);
        return "users";
    }

    @GetMapping("users/nextpage")
    public String loadNextPage(Model model, @RequestParam(defaultValue = "1") String page) {
        int p = Integer.parseInt(page);
        var users = userService.getPage(p, 10);
        model.addAttribute("nextpage", users.getLast().getId());
        model.addAttribute("users", users);
        return "users-nextpage";
    }

    @GetMapping("/users/{username}/edit")
    public String editUserProfile(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "edit-user-profile";
    }

    @GetMapping("messages")
    public String getMessages(Model model, @AuthenticationPrincipal OAuth2User user) {
        List<Message> messages;
        if (user == null) {
            messages = messageService.getPublicMessages();
        } else {
            messages = messageService.getAllMessages();
        }
        model.addAttribute("messages", messages);
        return "messages";
    }

    @GetMapping("/messages/new")
    public String newMessage(Model model) {
        model.addAttribute("message", new Message());
        return "createMessage";
    }

    @PostMapping("/messages/new")
    public String createNewMessage(@AuthenticationPrincipal OAuth2User principal, @ModelAttribute("message") Message message) {
        try {
            messageService.createMessage(message, principal);
            return "redirect:/web/messages";
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/messages/{id}/delete")
    public String deleteMessageById(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id) {
        try {
            User user = userService.findByGithubLogin(principal.getAttribute("login"));
            Message message = messageService.getMessageById(id).orElseThrow();
            if (message.getUser().equals(user)) {
                messageService.deleteMessage(id);
            }
            return "redirect:/web/messages";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/messages/{id}/edit")
    public String editMessage(@PathVariable Long id, Model model) {
        Optional<Message> messageOptional = messageService.getMessageById(id);

        if (messageOptional.isPresent()) {
            model.addAttribute("message", messageOptional.get());
            return "editMessage";
        } else {
            return "error";
        }
    }

    @PostMapping("/messages/{id}/edit")
    public String updateMessageById(@PathVariable Long id, @ModelAttribute("message") Message messageDetails, Model model, User user) {
        try {
            Optional<Message> existingMessageOptional = messageService.getMessageById(id);
            if (existingMessageOptional.isEmpty()) {
                return "error";
            }

            Message existingMessage = existingMessageOptional.get();
            messageDetails.setUser(existingMessage.getUser());
            existingMessage.setStatusPrivate(messageDetails.isStatusPrivate());
            messageService.updateMessage(id, existingMessage,user);

            return "redirect:/web/mymessages";
        } catch (Exception e) {
            return "error";
        }
    }
    @GetMapping("/mymessages")
    public String displayCurrentUserMessages(Model model,@AuthenticationPrincipal OAuth2User principal) {
        List<Message> userMessages = messageService.getMessagesForCurrentUser(principal);
        model.addAttribute("messages", userMessages);
        return "mymessages";
    }

}
