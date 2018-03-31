package com.simple;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.simple.dao")
public class PersonalwebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalwebsiteApplication.class, args);
    }
}
