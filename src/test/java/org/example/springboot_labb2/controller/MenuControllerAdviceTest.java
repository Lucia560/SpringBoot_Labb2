package org.example.springboot_labb2.controller;

import org.example.springboot_labb2.controller.web.MenuControllerAdvice;
import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuControllerAdviceTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private MenuControllerAdvice menuControllerAdvice;

    private OAuth2User principal;
    private User user;

    @BeforeEach
    void setUp() {
        principal = new DefaultOAuth2User(Collections.emptyList(), Map.of("login", "userLogin"), "login");

        user = new User();
        user.setUsername("TestUser");
        user.setGithubLogin("userLogin");
    }

    @Test
    void addAttributes_UserLoggedIn_ShouldAddAuthenticatedUserToModel() {
        when(userService.findByGithubLogin("userLogin")).thenReturn(user);

        menuControllerAdvice.addAttributes(model, principal);

        verify(model).addAttribute(eq("menu"), any(MenuControllerAdvice.MenuModel.class));
    }

    @Test
    void addAttributes_UserNotLoggedIn_ShouldAddNotAuthenticatedToModel() {
        menuControllerAdvice.addAttributes(model, null);

        verify(model).addAttribute(eq("menu"), any(MenuControllerAdvice.MenuModel.class));
    }
}