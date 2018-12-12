/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月26日 下午4:19:30
 * @version V1.0
 */

package com.iqb.consumer.cache.layer.kv;

import java.io.IOException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.iqb.consumer.cache.layer.config.RedisConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class KVCacheImpl<V> implements KVCache<V> {

    private static final Logger logger = LoggerFactory.getLogger(KVCacheImpl.class);
    public static final int CACHE_ONE_DAY = 24 * 60 * 60;
    private static final String KV_CACHE_PREFIX = "kv.";
    @Resource
    private RedisConfig redisConfig;

    @Override
    public V get(String key, Class<V> clazz) {
        key = KV_CACHE_PREFIX + key;
        logger.trace("get: key={}", key);
        JedisPool pool = redisConfig.getJedisPool();
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String value = jedis.get(key);
            if (value == null) {
                return null;
            }
            V c = redisConfig.getObjectMapper().readValue(value, clazz);
            return c;
        } catch (IOException ioe) {
            logger.error("error in get: key={}", key, ioe);
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
        return null;
    }

    @Override
    public void put(String key, V value) {
        key = KV_CACHE_PREFIX + key;
        logger.trace("put: [key={}, value={}]", key, value);
        JedisPool pool = redisConfig.getJedisPool();
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String s = redisConfig.getObjectMapper().writeValueAsString(value);
            jedis.set(key, s);
            // setting the TTL in seconds
            jedis.expire(key, redisConfig.getDefaultTTL());
        } catch (IOException ioe) {
            logger.error("error in put: key={}, value={}", key, value, ioe);
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
    }

    @Override
    public void put(String key, V value, int ttl) {
        key = KV_CACHE_PREFIX + key;
        logger.trace("put: [key={}, value={}], ttl={}", key, value, ttl);
        JedisPool pool = redisConfig.getJedisPool();
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String s = redisConfig.getObjectMapper().writeValueAsString(value);
            jedis.set(key, s);
            // setting the TTL in seconds
            jedis.expire(key, ttl);
        } catch (IOException ioe) {
            logger.error("error in put: key={}, value={}", key, value, ioe);
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
    }

    @Override
    public void remove(String key) {
        key = KV_CACHE_PREFIX + key;
        logger.trace("remove: key={}", key);
        JedisPool pool = redisConfig.getJedisPool();
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.del(key);
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
    }

    @Override
    public void put(String key, String value) {
        key = KV_CACHE_PREFIX + key;
        logger.trace("put: [key={}, value={}]", key, value);
        JedisPool pool = redisConfig.getJedisPool();
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
            // setting the TTL in seconds
            jedis.expire(key, redisConfig.getDefaultTTL());
        } catch (Exception ioe) {
            logger.error("error in put: key={}, value={}", key, value, ioe);
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
    }

    @Override
    public String get(String key) {
        key = KV_CACHE_PREFIX + key;
        logger.trace("get: key={}", key);
        JedisPool pool = redisConfig.getJedisPool();
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String value = jedis.get(key);
            return value;
        } catch (Exception ioe) {
            logger.error("error in get: key={}", key, ioe);
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
        return null;
    }

    @Override
    public void put(String key, String value, int ttl) {
        key = KV_CACHE_PREFIX + key;
        logger.trace("put: [key={}, value={}], ttl={}", key, value, ttl);
        JedisPool pool = redisConfig.getJedisPool();
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
            jedis.expire(key, ttl);
        } catch (Exception ioe) {
            logger.error("error in put: key={}, value={}", key, value, ioe);
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
    }

}
