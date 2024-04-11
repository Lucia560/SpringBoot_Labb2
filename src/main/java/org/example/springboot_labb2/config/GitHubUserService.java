package org.example.springboot_labb2.config;

import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * @author Angela Gustafsson, anggus-1
 */
@Service
public class GitHubUserService extends DefaultOAuth2UserService {
    @Autowired private UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        if(userRepository.findByGithubLogin(oAuth2User.getAttribute("login")).isEmpty()) {
            User user = new User();
            user.setGithubLogin(oAuth2User.getAttribute("login"));
            user.setUsername(oAuth2User.getAttribute("login"));
            userRepository.save(user);
        }
        return oAuth2User;
    }
}
