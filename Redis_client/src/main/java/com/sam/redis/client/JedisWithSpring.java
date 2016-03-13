package com.sam.redis.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class JedisWithSpring {

    @Resource
    protected RedisTemplate redisTemplate;


    @Resource(name = "redisTemplate")
    private RedisTemplate<String, String> template; // inject the template as ListOperations
    //�������Ϊʲô����ע�롣��Ҫ�ο�AbstractBeanFactory doGetBean
    //super.setValue(((RedisOperations) value).opsForValue());����һ�д���  ����һ��editor
    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> vOps;

    public void save() {
        redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] key  = serializer.serialize("spring");
                byte[] value = serializer.serialize("spring_value");
                return connection.setNX(key, value);
            }
        });
    }

    public Object get(final String str) {
        return redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] key = redisTemplate.getStringSerializer().serialize(str);
                if (connection.exists(key)) {
                    byte[] value = connection.get(key);
                    return redisTemplate.getStringSerializer().deserialize(value);
                }
                return null;
            }
        });
    }



    @Test
    public void put() {
        redisTemplate.opsForValue().set("spring_simple","spring_value");
    }
    @Test
    public void get(){
        System.out.println(redisTemplate.opsForValue().get("spring_simple"));
    }
    @Test
    public void delete() {
        redisTemplate.delete("spring_simple");
    }

    @Test
    public void hPut() {
        redisTemplate.opsForHash().put("user","name","张三");
    }

    @Test
    public void hGet() {
        System.out.println(redisTemplate.opsForHash().get("user","name"));
    }

    @Test
    public void hDelete() {
        redisTemplate.opsForHash().delete("user", "name");
    }
    @Test
    public void keys() {
        System.out.println(redisTemplate.keys("*"));
    }

    @Test
    public void scan() {

    }
}