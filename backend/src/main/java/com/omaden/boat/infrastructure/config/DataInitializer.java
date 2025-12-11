package com.omaden.boat.infrastructure.config;

import com.omaden.boat.auth.domain.model.Role;
import com.omaden.boat.auth.domain.model.User;
import com.omaden.boat.auth.domain.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.create("admin", passwordEncoder.encode("admin123"), Role.ADMIN);
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("user1").isEmpty()) {
            User admin = User.create("user1", passwordEncoder.encode("user12345"), Role.USER);
            userRepository.save(admin);
        }
    }
}
