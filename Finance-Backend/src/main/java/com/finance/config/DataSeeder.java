package com.finance.config;

import com.finance.entity.*;
import com.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.save(User.builder().name("Admin User").email("admin@finance.com")
                        .password(passwordEncoder.encode("password123")).role(Role.ADMIN).active(true).build());

                userRepository.save(User.builder().name("Analyst User").email("analyst@finance.com")
                        .password(passwordEncoder.encode("password123")).role(Role.ANALYST).active(true).build());

                userRepository.save(User.builder().name("Viewer User").email("viewer@finance.com")
                        .password(passwordEncoder.encode("password123")).role(Role.VIEWER).active(true).build());

                System.out.println("Sample users seeded!");
            }
        };
    }
}