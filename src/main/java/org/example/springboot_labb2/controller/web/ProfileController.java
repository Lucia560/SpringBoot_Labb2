package org.example.springboot_labb2.controller.web;

import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Angela Gustafsson, anggus-1
 */
@Controller
public class ProfileController {
    @Autowired
    UserService userService;
    @GetMapping("/web/profile")
    public String getProfile(Model model, @AuthenticationPrincipal OAuth2User oAuth2User){
        User login = userService.findByGithubLogin(oAuth2User.getAttribute("login"));
        model.addAttribute("user", login);
        return "profile";
    }
    @PostMapping("/web/profile")
    public String postProfile(@AuthenticationPrincipal OAuth2User oAuth2User, @ModelAttribute User user){
        User login = userService.findByGithubLogin(oAuth2User.getAttribute("login"));
        userService.updateUser(login.getId(), user);
        return "redirect:/web/profile";
    }
}
