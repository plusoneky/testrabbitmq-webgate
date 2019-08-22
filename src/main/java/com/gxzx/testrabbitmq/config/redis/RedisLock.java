package com.gxzx.testrabbitmq.config.redis;

import java.util.Random;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisLock {
	private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);;
	    
	    @Resource
		private StringRedisTemplate redisTemplate;

	    /**
	     * Lock key path.
	     */
	    private String lockKey;

	    /**
	     * 锁超时时间，防止线程在入锁以后，无限的执行等待，key的过期时间，单位（秒）
	     */
	    private int expireSecs = 60;

	    /**
	     * 锁等待时间，防止线程饥饿，lock的超时时间，单位（毫秒）
	     */
	    private int timeoutMsecs = 10 * 1000;

	    private volatile boolean locked = false;
	    
	    public static final Random random = new Random();
	    
	    public static final byte[] LOCKED = new byte[]{1};

	    /**
	     * Detailed constructor with default acquire timeout 10000 msecs and lock expiration of 60000 msecs.
	     *
	     * @param lockKey lock key (ex. account:1, ...)
	     */
	    public RedisLock(StringRedisTemplate redisTemplate, String lockKey) {
	        this.redisTemplate = (StringRedisTemplate) redisTemplate;
	        this.lockKey = lockKey + "_lock";
	    }

	    /**
	     * Detailed constructor with default lock expiration of 60000 msecs.
	     *
	     */
	    public RedisLock(StringRedisTemplate redisTemplate, String lockKey, int timeoutSecs) {
	        this(redisTemplate, lockKey);
	        this.timeoutMsecs = timeoutSecs*1000;
	    }


	    /**
	     * RedisLock构造器
	     * @param redisTemplate
	     * @param lockKey  锁的KEY
	     * @param timeoutMsecs  锁的超时时间，单位：秒
	     * @param expireMsecs  锁的KEY在Redis里的失效时间，单位：秒
	     */
	    public RedisLock(StringRedisTemplate redisTemplate, String lockKey, int timeoutSecs, int expireSecs) {
	        this(redisTemplate, lockKey, timeoutSecs);
	        this.expireSecs = expireSecs;
	    }

	    /**
	     * @return lock key
	     */
	    public String getLockKey() {
	        return lockKey;
	    }

	    private String get(final String key) {
	        Object obj = null;
	        try {
	            obj = redisTemplate.execute(new RedisCallback<Object>() {
	                @Override
	                public Object doInRedis(RedisConnection connection) throws DataAccessException {
	                    StringRedisSerializer serializer = new StringRedisSerializer();
	                    byte[] data = connection.get(serializer.serialize(key));
	                    connection.close();
	                    if (data == null) {
	                        return null;
	                    }
	                    return serializer.deserialize(data);
	                }
	            });
	        } catch (Exception e) {
	            logger.error("get redis error, key : {}"+ key);
	        }
	        return obj != null ? obj.toString() : null;
	    }

	    private boolean setNX(final String key, final int expireSeconds) {
	        Object obj = null;
	        try {
	            obj = redisTemplate.execute(new RedisCallback<Object>() {
	                @Override
	                public Object doInRedis(RedisConnection connection) throws DataAccessException {
	                    StringRedisSerializer serializer = new StringRedisSerializer();
						boolean success = connection.setNX(serializer.serialize(key), LOCKED);
	                    connection.expire(serializer.serialize(key), expireSeconds);
	                    connection.close();
	                    return success;
	                }
	            });
	        } catch (Exception e) {
	            logger.error("setNX redis error, key : {}"+key);
	        }
	        return obj != null ? (Boolean) obj : false;
	    }

	    private String getSet(final String key, final String value) {
	        Object obj = null;
	        try {
	            obj = redisTemplate.execute(new RedisCallback<Object>() {
	                @Override
	                public Object doInRedis(RedisConnection connection) throws DataAccessException {
	                    StringRedisSerializer serializer = new StringRedisSerializer();
	                    byte[] ret = connection.getSet(serializer.serialize(key), serializer.serialize(value));
	                    connection.close();
	                    return serializer.deserialize(ret);
	                }
	            });
	        } catch (Exception e) {
	            logger.error("setNX redis error, key : {}"+ key);
	        }
	        return obj != null ? (String) obj : null;
	    }
	    
	    /**
	     * 获得 lock.
	     * 实现思路: 主要是使用了redis 的setnx命令,缓存了锁.
	     * reids缓存的key是锁的key,所有的共享, value是锁的到期时间(注意:这里把过期时间放在value了,没有时间上设置其超时时间)
	     * 执行过程:
	     * 1.通过setnx尝试设置某个key的值,成功(当前没有这个锁)则返回,成功获得锁
	     * 2.锁已经存在则获取锁的到期时间,和当前时间比较,超时的话,则设置新的值
	     *
	     * @return true if lock is acquired, false acquire timeouted
	     * @throws InterruptedException in case of thread interruption
	     */
	    public synchronized boolean lock() throws InterruptedException {
	        long nano = System.nanoTime();
	        long timeout = timeoutMsecs;	        
	        while ((System.nanoTime() - nano) < timeout) {
	            if (this.setNX(lockKey, expireSecs)) {
	                locked = true;
	                return true;
	            }
	            Thread.sleep(3,random.nextInt(500));

	        }
	        return false;
	    }


	    /**
	     * Acqurired lock release.
	     */
	    public synchronized void unlock() {
	        if (locked) {
	            redisTemplate.delete(lockKey);
	            locked = false;
	        }
	    }
}
