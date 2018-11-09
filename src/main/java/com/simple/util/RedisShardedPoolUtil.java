package com.simple.util;

import com.simple.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

/**
 * Create by S I M P L E on 2018/06/24 10:51:52
 */
@Slf4j
public class RedisShardedPoolUtil {

    //设置一个key，（会覆盖key相同的key）
    public static String set(String key, String value) {
        ShardedJedis jedis = null;
        String result;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            RedisShardedPool.close(jedis);
            return null;
        }
        RedisShardedPool.close(jedis);
        return result;
    }

    //删除一个key
    public static Long del(String key) {
        ShardedJedis jedis = null;
        Long result;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error", key, e);
            RedisShardedPool.close(jedis);
            return null;
        }
        RedisShardedPool.close(jedis);
        return result;
    }

    //将一个key设置有效时间
    public static Long expire(String key, int exTime) {
        ShardedJedis jedis = null;
        Long result;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("set key:{}  error", key, e);
            RedisShardedPool.close(jedis);
            return null;
        }
        RedisShardedPool.close(jedis);
        return result;
    }

    //获取一个key的value
    public static String get(String key) {
        ShardedJedis jedis = null;
        String result;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("set key:{} error", key, e);
            RedisShardedPool.close(jedis);
            return null;
        }
        RedisShardedPool.close(jedis);
        return result;
    }

    //创建一个key，如果已经存在则跳过，并且设置其有效时间
    public static String setEx(String key, int exTime, String value) {
        ShardedJedis jedis = null;
        String result;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("exset key:{} value:{} error", key, value, e);
            RedisShardedPool.close(jedis);
            return null;
        }
        RedisShardedPool.close(jedis);
        return result;
    }
}
