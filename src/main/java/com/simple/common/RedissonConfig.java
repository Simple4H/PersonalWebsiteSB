package com.simple.common;

import com.simple.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Objects;


/**
 * Create By S I M P L E On 2018/11/24 15:58:08
 */
@Slf4j
public class RedissonConfig {

    private static String redisIp = PropertiesUtil.getProperty("");

    private static Integer redisPort = Integer.parseInt(Objects.requireNonNull(PropertiesUtil.getProperty("")));

    public static Redisson getRedisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        return (Redisson) redisson;
    }



}
