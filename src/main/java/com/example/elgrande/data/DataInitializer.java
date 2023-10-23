package com.example.elgrande.data;

import com.example.elgrande.model.enums.enums_user.UserLevel;
import com.example.elgrande.model.user.User;
import com.example.elgrande.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    @Autowired
    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        User user1 = new User("User1", "password1", "MALE",24,70, 170, UserLevel.BEGINNER);
        User user2 = new User("User2", "password2", "FEMALE",26,75, 175, UserLevel.INTERMEDIATE);
        User user3 = new User("User3", "password3", "MALE",30,80, 180, UserLevel.PROFESSIONAL);
        User user4 = new User("User4", "password4", "FEMALE",34,85, 185, UserLevel.ELITE);
        User user5 = new User("User5", "password5", "MALE",44,90, 190, UserLevel.EXPERT);

        userRepository.saveAll(List.of(user1, user2, user3, user4, user5));
    }
}