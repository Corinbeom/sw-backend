package com.sw.config;

import com.sw.model.User;
import com.sw.service.UserService;
import jakarta.persistence.Column;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private final UserService userService;

    public DataLoader(UserService userService) {
        this.userService = userService;
    }

    public void run(ApplicationArguments args) {
        User user = new User();
        user.setUserEmail("TestEmail@gmail.com");
        user.setNickName("TestName");

        String testHashedPassword = BCrypt.hashpw("1234", BCrypt.gensalt());
        user.setPassword(testHashedPassword);

        userService.setTestUser(user);

    }
}
