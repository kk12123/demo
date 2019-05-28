package com.seckill.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Component
public class RedisUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);
    @Autowired
    private  RedisConfig redisConfig;

    @PostConstruct
    public void init(){
        LOGGER.info("初始化开始，RedisConfig=：{}",redisConfig);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        jedisPoolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        jedisPoolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);
        jp = new JedisPool(jedisPoolConfig, redisConfig.getHost(), redisConfig.getPort(),
                redisConfig.getTimeout() * 1000, redisConfig.getPassword(), 0);
    }


    private static JedisPool jp;

    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 分布式锁 加锁
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean lock(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jp.getResource();
            String result = jedis.set(key, value, "NX", "PX", 60 * 1000);
            if ("OK".equals(result)) {
                return true;
            }
            return false;
        }catch (Exception e){
            if(jedis != null){
                jedis.close();
            }
            return false;
        }

    }

    /**
     * 分布式锁 解锁
     * @param lockKey
     * @param requestId
     * @return
     */
    public static boolean release(String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Jedis jedis = null;
        try {
            jedis = jp.getResource();
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        }catch (Exception e){
            if(jedis!= null){
                jedis.close();
            }
            return false;
        }

    }

    /**
     *
     * @param key
     * @return
     */
    public static String get(String key){
        Jedis jedis = null;
        try{
            jedis = jp.getResource();
            return jedis.get(key);
        }catch (Exception e){
            if(jedis!= null){
                jedis.close();
            }
            return null;
        }

    }

    /**
     *
     * @param key
     * @param value
     */
    public static void set(String key,String value){
        Jedis jedis = null;
        try {
            jedis = jp.getResource();
            jedis.set(key,value);
        }catch (Exception e){
            if(null != jedis){
                jedis.close();
            }
        }
    }

    /**
     *
     * @param key
     * @param value
     */
    public static void bitSet(String key,String value){
        Jedis jedis = null;
        try {
            jedis = jp.getResource();
            jedis.setbit(key,0,value);
        }catch (Exception e){
            if(null != jedis){
                jedis.close();
            }
        }
    }

    /**
     *
     * @param key
     */
    public static boolean getbit(String key){
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = jp.getResource();
            result = jedis.getbit(key,0);
            return result;
        }catch (Exception e){
            if(null != jedis){
                jedis.close();
            }
            return result;
        }
    }

    /**
     *
     * @param key
     */
    public static void del(String key){
        Jedis jedis = null;
        try {
            jedis = jp.getResource();
            jp.getResource().del(key);
        }catch (Exception e){
            if(null !=  jedis){
                jedis.close();
            }
        }

    }

}







