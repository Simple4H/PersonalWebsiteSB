package com.simple.personalwebsite;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PersonalwebsiteApplicationTests {

    public static void main(String[] args) {
        for (int i = 0; i <= 10; i++) {
            int token = (int) (Math.random() * 9000 + 1000);
            log.info("result:{}", token);
        }
        final int java3y = new Random().nextInt(20);
        System.out.println(java3y);
    }

    @Test
    public void test1(){
        final int java3y = new Random().nextInt(20);
        System.out.println(java3y);
    }

}
