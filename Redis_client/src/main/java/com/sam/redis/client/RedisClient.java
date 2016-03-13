package com.sam.redis.client;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

import java.util.Collections;
import java.util.List;

/**
 * Created by home on 2016/2/6.
 */
public class RedisClient {
    Jedis jedis;

    @Before
    public void init() {
        jedis  = new Jedis("master",6379);
        jedis.auth("sam-sho");//授权
//        jedis.disconnect();
    }

    /**
     * 字符串
     */
    @Test
    public void testNormal() {
        String result = jedis.set("clientTest", "clientTest");
        System.out.println("Simple SET: " + result);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++=");
        System.out.println("Result:  " + jedis.get("clientTest"));
        System.out.println("Result:  " + jedis.get("a"));
    }

    /**
     * List链表
     */
    @Test
    public void testList() {
        //存储数据到列表中
        jedis.lpush("tutorial-list", "Redis");
        jedis.lpush("tutorial-list", "Mongodb");
        jedis.lpush("tutorial-list", "Mysql");
        // 获取存储的数据并输出
        List<String> list = jedis.lrange("tutorial-list", 0 ,5);
        for(int i=0; i<list.size(); i++) {
            System.out.println("Stored string in redis:: "+list.get(i));
        }
    }

    /**
     * 事务
     */
    @Test
    public void test2Trans() {

        Transaction tx = jedis.multi();
        tx.set("t", "t");
        tx.set("t", "t");
        List<Object> results = tx.exec();

    }

    /**
     * 管道
     */
    @Test
    public void test3Pipelined() {
        Pipeline pipeline = jedis.pipelined();

        pipeline.set("p" + 1, "p" + 1);
        pipeline.set("p" + 2, "p" + 2);
        List<Object> results = pipeline.syncAndReturnAll();
        System.out.println(results);
        System.out.println(jedis.get("p1"));

    }
}
