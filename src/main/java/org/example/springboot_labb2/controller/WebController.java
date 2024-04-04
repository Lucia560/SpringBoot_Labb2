package org.example.springboot_labb2.controller;


import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web")
public class WebController {

    private final UserService userService;

    public WebController( UserService userService) {
        this.userService = userService;
    }

    //Users
    @GetMapping("users")
    public String users(Model model){
        var users= userService.getPage(0,10);
                //userService.getAllUsers().stream().map(User::getUsername).toList();
        model.addAttribute("nextpage",users.getLast().getId());
        model.addAttribute("usersNames",users);
        return "users";  // should be "users/users"??
    }

    @GetMapping("/users/{username}/edit")
    public String editUserProfile(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "edit-user-profile";
    }

    @GetMapping("users/nextpage")
    public String loadPages(Model model, @RequestParam (defaultValue = "1") String page){
       var users= userService.getPage(0,10);
       int p= Integer.parseInt(page);
       model.addAttribute("nextpage", users.getLast().getId());
       model.addAttribute("users",users);
       return "users-nextpage";

    }





}
