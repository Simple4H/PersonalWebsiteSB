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
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= i; j = j + 1) {
                System.out.print("i"+"j"+"="+i*j);
                System.out.print("");
            }
            System.out.println();
        }
    }

}
