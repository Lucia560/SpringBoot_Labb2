package org.example.springboot_labb2;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // check this if is on the right class, you dont have the runner here
@EnableCaching
public class SpringBootLabb2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLabb2Application.class, args);
    }
}
