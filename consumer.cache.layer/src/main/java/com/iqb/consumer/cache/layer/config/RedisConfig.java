/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月26日 下午2:08:03
 * @version V1.0
 */

package com.iqb.consumer.cache.layer.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Configuration
public class RedisConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
    // Redis 服务器IP
    @Value("${redis.ip}")
    private String ip;
    // Redis 服务器端口
    @Value("${redis.port}")
    private int port;
    // Redis 可用连接实例jedis的最大数目
    @Value("${redis.maxActive}")
    private int max_active;
    // Redis 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例
    @Value("${redis.maxIdle}")
    private int max_idle;
    // Redis 等待可用连接的最大时间，单位毫秒;如果超过等待时间，则直接抛出JedisConnectionException
    @Value("${redis.maxWait}")
    private int max_wait;
    // Redis 超时
    @Value("${redis.timeOut}")
    private int timeout;
    // Redis 默认过期时间
    @Value("${redis.ttl}")
    private int defaultTTL;
    @Value("${redis.database}")
    private int database;
    private JedisPool jedisPool = null;

    public String getIp() {
        return ip;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMax_active() {
        return max_active;
    }

    public void setMax_active(int max_active) {
        this.max_active = max_active;
    }

    public int getMax_idle() {
        return max_idle;
    }

    public void setMax_idle(int max_idle) {
        this.max_idle = max_idle;
    }

    public int getMax_wait() {
        return max_wait;
    }

    public void setMax_wait(int max_wait) {
        this.max_wait = max_wait;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDefaultTTL() {
        return defaultTTL;
    }

    public void setDefaultTTL(int defaultTTL) {
        this.defaultTTL = defaultTTL;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @PostConstruct
    public void initJedisPool() {
        logger.info("init Jedis Pool ...");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(max_active);
        poolConfig.setMaxIdle(max_idle);
        poolConfig.setMaxWaitMillis(max_wait);
        // jedisPool = new JedisPool(poolConfig, ip, port, timeout);
        jedisPool = new JedisPool(poolConfig, ip, port, timeout, null, database);
        logger.info("init Jedis Pool Done!");
    }

    @PreDestroy
    public void destroyJedisPool() {
        logger.info("destroy Jedis Pool ...");
        jedisPool.destroy();
    }

    public ObjectMapper getObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return om;
    }
}
