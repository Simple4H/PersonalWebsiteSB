package com.simple.common;

import com.simple.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Create by S I M P L E on 2018/06/24 10:51:14
 */
public class RedisShardedPool {

    // Jedis连接池
    private static ShardedJedisPool jedisPool;

    // 最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));

    // 在jedis pool中，最大Idle空闲jedis实例的个数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));

    // 在jedis pool中，最小Idle空闲jedis实例的个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "2"));

    // 在borrow（取）一个jedis实例的时候，是否需要进行验证
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));

    // 在return（返回）一个jedis实例的时候，是否需要进行验证
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.return", "false"));

    // redis IP1
    private static String redisIp1 = PropertiesUtil.getProperty("redis.ip1");

    // redis port1
    private static Integer redisPort1 = Integer.parseInt(Objects.requireNonNull(PropertiesUtil.getProperty("redis.port1")));

    // redis IP2
    private static String redisIp2 = PropertiesUtil.getProperty("redis.ip2");

    // redis port2
    private static Integer redisPort2 = Integer.parseInt(Objects.requireNonNull(PropertiesUtil.getProperty("redis.port2")));


    // 初始化连接池
    private static void initJedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        // 连接耗尽时候，是否阻塞，true阻塞到超时，false则是抛出异常
        jedisPoolConfig.setBlockWhenExhausted(true);
        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(redisIp1, redisPort1, 1000 * 2);
        JedisShardInfo jedisShardInfo2 = new JedisShardInfo(redisIp2, redisPort2, 1000 * 2);
        List<JedisShardInfo> jedisShardInfos = new ArrayList<>(2);
        jedisShardInfos.add(jedisShardInfo1);
        jedisShardInfos.add(jedisShardInfo2);

        // 设置分片算法，MURMUR_HASH。一致性算法
        jedisPool = new ShardedJedisPool(jedisPoolConfig, jedisShardInfos, Hashing.MURMUR_HASH);
    }

    static {
        System.out.println(maxTotal);
        initJedisPool();
        System.out.println(maxTotal);
    }

    // 开放到外部供使用
    // 从连接池中获取一个实例
    public static ShardedJedis getJedis() {
        return jedisPool.getResource();
    }

    // 将实例放回连接池
    public static void close(ShardedJedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    // 程序关闭时，需要调用关闭方法
    public static void end() {
        if (jedisPool != null) {
            jedisPool.destroy();
        }
    }

    public static void main(String[] args) {

        ShardedJedis jedis = jedisPool.getResource();

        for (int i = 0; i < 10; i++) {
            jedis.set("key" + i, "value" + i);
        }
        jedisPool.close();
    }
}
