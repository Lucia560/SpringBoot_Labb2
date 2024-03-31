package org.example.springboot_labb2.controller;


import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class WebController {

    private final UserRepository userRepository;

    public WebController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("users")
    public String users(Model model){
        var users= userRepository.findAll().stream().map(User::getUsername).toList();
        model.addAttribute("usersNames",users); //add users count?
        return "users";
    }

   // show static welcome page
   // show log in /register
   //show messages , write new , edit
   //show user profil, edit , delete ---admin view /user view





}
