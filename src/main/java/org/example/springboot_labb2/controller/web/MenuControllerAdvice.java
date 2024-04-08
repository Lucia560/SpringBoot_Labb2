package org.example.springboot_labb2.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author Angela Gustafsson, anggus-1
 */
@ControllerAdvice
public class MenuControllerAdvice {
    record MenuModel(boolean authenticated, String username) {
    }

    @ModelAttribute
    public void addAttributes(Model model, @AuthenticationPrincipal OAuth2User user) {

        if (user != null) {
            model.addAttribute("menu", new MenuModel(true, user.getAttribute("login")));
        } else {
            model.addAttribute("menu", new MenuModel(false, null));
        }
    }
}

