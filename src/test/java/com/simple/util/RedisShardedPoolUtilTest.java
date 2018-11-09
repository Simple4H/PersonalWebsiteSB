package com.simple.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;


/**
 * Create By S I M P L E On 2018/11/09 13:46:50
 */
@Slf4j
public class RedisShardedPoolUtilTest {

    @Test
    public void set() {
        for (int i = 0; i < 10; i++) {
            RedisShardedPoolUtil.set("keys" + i, "value" + i);
        }
    }

    @Test
    public void get() {
        for (int i = 0; i < 10; i++) {
            String a = String.valueOf(i);
            log.warn("key:{},value is {}", a, RedisShardedPoolUtil.get("keys" + a));
        }
    }

    @Test
    public void del() {
        for (int i = 0; i < 10; i++) {
            String a = String.valueOf(i);
            RedisShardedPoolUtil.del("keys" + a);
        }
        for (int i = 0; i < 10; i++) {
            String a = String.valueOf(i);
            log.warn("key:{},value is {}", a, RedisShardedPoolUtil.get("keys" + a));
        }
    }

}