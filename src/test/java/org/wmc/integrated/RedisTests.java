package org.wmc.integrated;


import org.wmc.integrated.cache.redis.RedisLockUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


class RedisTests extends BaseTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate1;
    @Autowired
    private RedisLockUtil redisLockUtil;

    @Test
    public void contextLoads() throws ExecutionException, InterruptedException {
        System.out.println("redisTemplate:" + redisTemplate);
        System.out.println("redisTemplate1:" + redisTemplate1);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Boolean> aaabbb1 = executorService.submit(() -> {
            boolean aaabbb = redisLockUtil.lock("aaabbb", 60);
            System.out.println("1111111111:" + aaabbb);
            return aaabbb;
        });
        Future<Boolean> aaabbb2 = executorService.submit(() -> {
            boolean aaabbb = redisLockUtil.lock("aaabbb", 60);
            System.out.println("22222222222:" + aaabbb);
            return aaabbb;
        });
        System.out.println("aaabbb1:" + aaabbb1.get());
        System.out.println("aaabbb2:" + aaabbb2.get());
        redisLockUtil.unLock1("aaabbb");
        boolean aaabbb = redisLockUtil.lock("aaabbb", 60);
        System.out.println("22222222222:" + aaabbb);
    }
}
