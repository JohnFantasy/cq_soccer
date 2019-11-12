package com.laofaner.cq_soccer.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 *
 */
public class JedisUtil {
    public static String getJedisString(JedisPool jedisPool, int database, String key) {
        String str = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(database);
            str = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return str;
    }

    public static void setRedisString(JedisPool jedisPool, int database, String key, String value, Integer seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(database);
            jedis.set(key, value);
            if (seconds != null) {
                jedis.expire(key, seconds);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static Long deleteByKey(JedisPool jedisPool, int database, String key) {
        Jedis jedis = null;
        Long l = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(database);
            l = jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return l;
    }

    public static Set<String> getKeys(JedisPool jedisPool, int database, String key) {
        Set<String> keySet = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(database);
            keySet = jedis.keys(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return keySet;
    }

}
