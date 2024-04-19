package org.example.springboot_labb2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * @author Angela Gustafsson, anggus-1
 */
@Configuration
public class SecurityConfig {
    @Autowired
    GitHubUserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        return http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/web/messages", "/webjars/**", "/", "/error", "/*.css", "/*.js").permitAll();
            auth.anyRequest().authenticated();
        }).oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/web/messages").permitAll()
                .userInfoEndpoint(e -> e.userService(userService)))
                .build();
    }


   @Bean
   public WebClient webClient() {
       return WebClient.builder()
               .build();
   }

}
