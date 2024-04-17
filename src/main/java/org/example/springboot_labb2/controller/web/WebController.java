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

@Controller
@RequestMapping("/web")
public class WebController {

    private final UserService userService;
    private final MessageService messageService;

    public WebController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
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

    @PostMapping("/messages/new")
    public String createNewMessage(@AuthenticationPrincipal OAuth2User principal, @ModelAttribute("message") Message message) {
        try {
            User user = userService.findByGithubLogin(principal.getAttribute("login"));
            messageService.createMessage(message, user);
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

    @PostMapping("/messages/{id}/edit")
    public String updateMessageById(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id, @ModelAttribute("message") Message messageDetails) {
        try {
            String githubLogin = principal.getAttribute("login");
            User user = userService.findByGithubLogin(githubLogin);
            messageService.updateMessage(id, messageDetails, user);
            return "redirect:/web/messages";
        } catch (Exception e) {
            return "error";
        }
    }
}
