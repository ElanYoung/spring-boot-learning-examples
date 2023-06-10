package com.starimmortal.excel;

import com.apifan.common.random.RandomSource;
import com.starimmortal.excel.pojo.UserDO;
import com.starimmortal.excel.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
@Slf4j
class EasyExcelApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void generateUserData() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<UserDO> userList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 1000000; i++) {
            UserDO user = UserDO.builder()
                    .username(RandomSource.personInfoSource().randomChineseName())
                    .password(RandomSource.personInfoSource().randomStrongPassword(16, false))
                    .nickname(RandomSource.personInfoSource().randomQQNickName())
                    .birthday(RandomSource.dateTimeSource().randomPastDate())
                    .phone(RandomSource.personInfoSource().randomChineseMobile())
                    .height(random.nextDouble() * 0.5 + 1.0)
                    .gender(RandomSource.numberSource().randomInt(1, 3))
                    .build();
            userList.add(user);
        }
        userService.saveBatch(userList, 500);
        stopWatch.stop();
        log.info("生成测试数据耗时：{}秒", stopWatch.getTotalTimeSeconds());
    }
}
