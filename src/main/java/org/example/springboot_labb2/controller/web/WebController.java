package org.example.springboot_labb2.controller.web;

import org.example.springboot_labb2.entity.Message;
import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.service.MessageService;
import org.example.springboot_labb2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web")
public class WebController {

    private final UserService userService;
    private final MessageService messageService;

    public WebController( UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("users")
    public String users(Model model){
        var users = userService.getPage(0,10);
        model.addAttribute("nextpage",users.getLast().getId());
        model.addAttribute("usersNames",users);
        return "users";
    }
    @GetMapping("users/nextpage")
    public String loadPages(Model model, @RequestParam (defaultValue = "1") String page) {
        int p = Integer.parseInt(page);
        var users = userService.getPage(p,10);
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

    @PostMapping("/messages/new")
    public String createMessage(@ModelAttribute("message") Message message) {
        messageService.saveMessage(message);
        return "redirect:/web/messages";
    }

    @PostMapping("/messages/{id}/delete")
    public String deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return "redirect:/web/messages";
    }

    @PostMapping("/messages/{id}/edit")
    public String editMessage(@PathVariable Long id, @ModelAttribute("message") Message message) {
        message.setId(id);
        messageService.saveMessage(message);
        return "redirect:/web/messages";
    }
}