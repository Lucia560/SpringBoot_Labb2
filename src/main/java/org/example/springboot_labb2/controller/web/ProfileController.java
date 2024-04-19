package org.example.springboot_labb2.controller.web;

import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Angela Gustafsson, anggus-1
 */
@Controller
public class ProfileController {
    final private Resource resourceFile;
    final private UserService userService;

    public ProfileController(@Value("classpath:static/defaultprofile.jpg") Resource resourceFile, UserService userService) {
        this.resourceFile = resourceFile;
        this.userService = userService;
    }

    @GetMapping("/web/profile")
    public String getProfile(Model model, @AuthenticationPrincipal OAuth2User oAuth2User){
        User login = userService.findByGithubLogin(oAuth2User.getAttribute("login"));
        model.addAttribute("user", login);
        return "profile";
    }

    @PostMapping("/web/profile")
    public String postProfile(@AuthenticationPrincipal OAuth2User oAuth2User, @ModelAttribute User user, @RequestParam("file") MultipartFile profilePicture) throws IOException {
        User login = userService.findByGithubLogin(oAuth2User.getAttribute("login"));
        if (profilePicture != null && !profilePicture.isEmpty()){
            user.setProfilePicture(profilePicture.getBytes());
        }
        userService.updateUser(login.getId(), user);
        return "redirect:/web/profile";
    }

    @GetMapping("/web/profile/picture/{id}")
    public ResponseEntity<byte[]> getProfile(@PathVariable("id") Long id) throws IOException {
        Optional<User> user = userService.findById(id);
        if (user.isPresent() && user.get().getProfilePicture() != null){
            return ResponseEntity.ok(user.get().getProfilePicture());
        } else{
            byte[] contentAsByteArray = resourceFile.getContentAsByteArray();
            return ResponseEntity.ok().body(contentAsByteArray);
        }
    }
}
