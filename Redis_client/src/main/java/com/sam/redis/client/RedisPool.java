package com.sam.redis.client;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis连接池
 */
public final class RedisPool {
    
    private static final String ADDR = "master";
    
    private static final int PORT = 6379;
    
    private static final String AUTH = "sam-sho";

    private static final int MAX_IDLE = 200;
    
    private static final int TIMEOUT = 10000;
    
    private static boolean TEST_ON_BORROW = true;
    
    private static JedisPool jedisPool = null;
    

    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(MAX_IDLE);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
        } catch (Exception e) {
        }
    }
    

    public synchronized static Jedis getJedis() {
        if (jedisPool != null) {
            Jedis jedis = jedisPool.getResource();
            return jedis;
        }
        return null;
    }
    

    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}