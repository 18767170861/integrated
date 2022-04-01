package org.wmc.integrated.cache.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

//redis分布式锁
@Component
public final class RedisLockUtil {

    private static final int defaultExpire = 60;

    @Autowired
    private RedisUtils redisService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private ValueOperations<Object, Object> valueOperations;

    private RedisLockUtil() {
        System.out.println("RedisLockUtil................................");
    }

    /**
     * 加锁
     *
     * @param key    redis key
     * @param expire 过期时间，单位秒
     * @return true:加锁成功，false，加锁失败
     */
    public boolean lock(String key, int expire) {
        Boolean result = valueOperations.setIfAbsent(key, "1", expire, TimeUnit.SECONDS);
        return result;
    }

    public boolean lock(String key) {
        return lock2(key, defaultExpire);
    }

    /**
     * 加锁
     *
     * @param key    redis key
     * @param expire 过期时间，单位秒
     * @return true:加锁成功，false，加锁失败
     */
    public boolean lock2(String key, int expire) {
        long value = System.currentTimeMillis() + expire;
        Boolean result = valueOperations.setIfAbsent(key, "1", expire, TimeUnit.SECONDS);
        if (result) {
            return true;
        }

        long oldExpireTime = Long.valueOf((String) redisService.get(key));
        if (oldExpireTime < System.currentTimeMillis()) {
            //超时
            long newExpireTime = System.currentTimeMillis() + expire;
            long currentExpireTime = Long.valueOf((String) redisService.getSet(key, String.valueOf(newExpireTime)));
            if (currentExpireTime == oldExpireTime) {
                return true;
            }
        }
        return false;
    }

    public void unLock1(String key) {
        redisService.del(key);
    }

    public void unLock2(String key) {
        long oldExpireTime = Long.valueOf((String) redisService.get(key));
        if (oldExpireTime > System.currentTimeMillis()) {
            redisService.del(key);
        }
    }

    // 以上分布式锁有安全隐患
    // 当第一个线程获得锁处理的时间大于超时时间 最后走解锁流程 将释放其它线程的锁
    // 因此下面的锁将通过值绑定来解决 释放锁的同时比较值 使用lua实现原子性HmacKey

    private static final String UNLOCK_LUA;

    private final static int NUM_KEYS = 1;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call('get', KEYS[1]) == ARGV[1] then");
        sb.append(" return redis.call('del', KEYS[1])");
        sb.append(" else return 0 end");
        UNLOCK_LUA = sb.toString();
    }

    /**
     * 尝试获取分布式锁
     *
     * @param key        锁名称
     * @param val        请求标识
     * @param expireTime 超期时间 秒
     * @return 是否获取成功
     */
    public boolean tryLock(String key, String val, int expireTime) {
        RedisCallback<Boolean> callback = (connection) ->
                connection.set(key.getBytes(), val.getBytes(), Expiration.seconds(expireTime), RedisStringCommands.SetOption.SET_IF_ABSENT);
        boolean result = redisTemplate.execute(callback);
        return result;
    }

    /**
     * 释放分布式锁
     *
     * @param key 锁名称
     * @param val 请求标识 只有标识相同才能解锁
     * @return 是否释放成功
     */
    public boolean releaseLock(String key, String val) {
        RedisCallback<Long> callback = (connection) ->
                connection.eval(UNLOCK_LUA.getBytes(), ReturnType.INTEGER, NUM_KEYS, key.getBytes(), val.getBytes());
        Long result = redisTemplate.execute(callback);
        return result != null && result > 0;
    }
}
