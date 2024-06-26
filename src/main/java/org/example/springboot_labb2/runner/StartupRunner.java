package org.example.springboot_labb2.runner;

import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

@Component
public class StartupRunner implements ApplicationRunner {

    private static final Logger LOG = Logger.getLogger(StartupRunner.class.getName());

    private final UserRepository userRepository;

    public StartupRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        LOG.info("Checking for users...");

        List<User> existingUsers = userRepository.findAll();
        if (existingUsers.isEmpty()) {
            User user = new User();
            user.setUsername("Joe Biden");
            user.setEmail("JoeBiden@example.com");
            userRepository.save(user);
            LOG.info("User initialized.");
        } else {
            LOG.info("Skipping user initialization as a user already exist.");
        }
    }
}