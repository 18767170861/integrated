package org.wmc.integrated.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Configuration
@EnableCaching
public class CaffeineConfig {

    @Bean(value = "caffeineCache")
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterWrite(60, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(1000)
                // 缓存的最大条数
                .maximumSize(10000)
                .build();
    }

    @Bean(value = "caffeineCache2")
    public Cache<String, Object> caffeineCache2() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterWrite(120, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(1000)
                // 缓存的最大条数
                .maximumSize(10000)
                .build();
    }

    //配置CacheManager
    @Bean(name = "caffeine")
    public CacheManager cacheManagerWithCaffeine() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        Caffeine caffeine = Caffeine.newBuilder()
                //cache的初始容量值
                .initialCapacity(100)
                //maximumSize用来控制cache的最大缓存数量，maximumSize和maximumWeight(最大权重)不可以同时使用，
                .maximumSize(1000)
                //最后一次写入或者访问后过久过期
                .expireAfterAccess(500, TimeUnit.SECONDS)
                //创建或更新之后多久刷新,需要设置cacheLoader
                .refreshAfterWrite(10, TimeUnit.SECONDS);
        cacheManager.setCaffeine(caffeine);
        cacheManager.setCacheLoader(cacheLoader());
        cacheManager.setCacheNames(Arrays.stream(CacheEnum.values()).map(cacheEnum -> cacheEnum.name()).collect(Collectors.toSet()));//根据名字可以创建多个cache，但是多个cache使用相同的策略
        cacheManager.setAllowNullValues(false);//是否允许值为空
        return cacheManager;
    }

    /**
     * 必须要指定这个Bean，refreshAfterWrite配置属性才生效
     */
    @Bean
    public CacheLoader<Object, Object> cacheLoader() {
        return new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object key) throws Exception {
                return null;
            }

            // 重写这个方法将oldValue值返回回去，进而刷新缓存
            @Override
            public Object reload(Object key, Object oldValue) throws Exception {
                System.out.println("--refresh--:" + key);
                return oldValue;
            }
        };
    }

    @Bean("caffeineCacheManager")
    @Primary
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ArrayList<CaffeineCache> caffeineCaches = new ArrayList<>();
        for (CacheEnum cacheEnum : CacheEnum.values()) {
            caffeineCaches.add(new CaffeineCache(cacheEnum.name(),
                    Caffeine.newBuilder().expireAfterWrite(Duration.ofSeconds(cacheEnum.getSecond()))
                            .initialCapacity(cacheEnum.getInitSize())
                            .maximumSize(cacheEnum.getMaxSize()).build()));
        }
        cacheManager.setCaches(caffeineCaches);
        return cacheManager;
    }

    @Bean("FIRST_CACHE")
    public org.springframework.cache.Cache firstCache(CacheManager cacheManager) {
        return cacheManager.getCache("FIRST_CACHE");
    }

    @Bean("SECOND_CACHE")
    public org.springframework.cache.Cache secondCache(CacheManager cacheManager) {
        return cacheManager.getCache("SECOND_CACHE");
    }
}
