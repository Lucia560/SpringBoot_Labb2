package org.example.springboot_labb2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * @author Angela Gustafsson, anggus-1
 */
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception{
        return http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/web/messages", "/webjars/**", "/", "/error").permitAll();
            auth.anyRequest().authenticated();
        }).oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/web/messages").permitAll()).csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable()).build();
    }

}
