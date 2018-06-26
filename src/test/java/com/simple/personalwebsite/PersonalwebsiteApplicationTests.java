package com.simple.personalwebsite;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PersonalwebsiteApplicationTests {

    public static void main(String[] args) {
        for (int i = 0; i <= 10; i++) {
            int token = (int) (Math.random() * 9000 + 1000);
            log.info("result:{}", token);
        }
    }

}
