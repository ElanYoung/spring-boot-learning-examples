package com.starimmortal.security.service.impl;

import com.starimmortal.security.pojo.UserDO;
import com.starimmortal.security.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void registerUser() {
        UserDO user = UserDO.builder()
                .username("admin")
                .nickname("威廉王子")
                .password("$2a$10$SD/NeFToLnnpWky0S4KFZuzkUKA1WZt3YZh8KDqlr2pPHcvyBlPSu")
                .gender(1)
                .status(1)
                .build();
        userService.save(user);
    }
}