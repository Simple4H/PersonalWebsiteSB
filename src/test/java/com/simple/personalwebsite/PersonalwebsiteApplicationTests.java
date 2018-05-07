package com.simple.personalwebsite;

import com.simple.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PersonalwebsiteApplicationTests {

    @Test
    public void MD5(){
        String md5Password = MD5Util.MD5EncodeUtf8("guest");
        log.info("password:{}",md5Password);
    }

}
