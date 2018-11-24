package com.simple.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * Create By S I M P L E On 2018/11/24 16:11:18
 */
@Slf4j
public class RedissonConfigTest {

    @Test
    public void Redisson() {
        RLock lock = RedissonConfig.getRedisson().getLock("test");
        boolean getLock = false;
        try {
            getLock = lock.tryLock(2, 5, TimeUnit.SECONDS);
            if (getLock) {
                log.info("获取到分布式锁,线程名:{}",Thread.currentThread().getName());
            } else {
                log.info("没有获取到分布式锁,线程名:{}",Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("获取分布式锁异常:{}", e);
        }finally {
            if (!getLock) {
                return;
            }
        }
    }
}