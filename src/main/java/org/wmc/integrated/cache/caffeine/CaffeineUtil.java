package org.wmc.integrated.cache.caffeine;

import com.github.benmanes.caffeine.cache.*;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class CaffeineUtil {
    public static void main(String[] args) throws InterruptedException {
        basedOnTime();

    }

    /**
     * 手动加载
     */
    public static void manualLoading() throws InterruptedException {
        Cache<String, String> cache = Caffeine.newBuilder()
                // 数量上限
                .maximumSize(1024)
                // 过期机制 2000毫秒
                .expireAfterWrite(2000, TimeUnit.MILLISECONDS)
                // 弱引用key
                .weakKeys()
                // 弱引用value
                .weakValues()
                // 剔除监听
                .removalListener((RemovalListener<String, String>) (key, value, cause) ->
                        System.out.println("key:" + key + ", value:" + value + ", 删除原因:" + cause.toString()))
                .build();
        System.out.println(cache.getClass());// class com.github.benmanes.caffeine.cache.BoundedLocalCache$BoundedLocalManualCache
        // 添加或者更新一个缓存元素
        cache.put("username", "afei");
        cache.put("password", "123456");
        System.out.println("size:" + cache.estimatedSize());
        // 查找一个缓存元素， 没有查找到的时候返回null
        System.out.println(cache.getIfPresent("username"));
        System.out.println(cache.getIfPresent("password"));
        // 查找缓存，如果缓存不存在则生成缓存元素,  如果无法生成则返回null
        System.out.println(cache.get("blog", key -> {
            return "数据库";
        }));
        ConcurrentMap<String, String> asMap = cache.asMap();
        System.out.println(asMap);
        // 移除一个缓存元素
        cache.invalidate("username");
        asMap = cache.asMap();
        System.out.println(asMap);
        //让缓存到期
        TimeUnit.SECONDS.sleep(3);
        System.out.println(cache.getIfPresent("password"));
    }

    /**
     * 自动加载
     */
    public static void autoLoad() throws InterruptedException {
        LoadingCache<Object, Object> cache = Caffeine.newBuilder()
                //基于时间失效->写入之后开始计时失效
                .expireAfterWrite(2000, TimeUnit.MILLISECONDS)
                //同步加载和手动加载的区别就是在构建缓存时提供一个同步的加载方法
                .build(new CacheLoader<Object, Object>() {
                    //单个 key 的值加载
                    @Nullable
                    @Override
                    public Object load(@NonNull Object key) {
                        System.out.println("--------load-----------------:" + key);
                        return key + "_" + System.currentTimeMillis();
                    }

                    //如果没有重写 loadAll 方法则默认的 loadAll 回循环调用 load 方法，一般重写优化性能
                    @Override
                    public @NonNull
                    Map<Object, Object> loadAll(@NonNull Iterable<?> keys) {
                        System.out.println("--------loadAll-----------------");
                        Map<Object, Object> data = new HashMap<>();
                        for (Object key : keys) {
                            data.put(key, key + "_all_" + System.currentTimeMillis());
                        }
                        return data;
                    }
                });


        // 查找缓存，如果缓存不存在则生成缓存元素, 如果无法生成则返回null
        Object value = cache.get("key1");
        Object value2 = cache.get("key2");
        System.out.println(value);
        System.out.println(value2);

        TimeUnit.SECONDS.sleep(3);

        // 批量查找缓存，如果缓存不存在则生成缓存元素
        Map<Object, Object> all = cache.getAll(Arrays.asList("key1", "key2", "key3"));
        System.out.println(all);

        cache.put("aaa", "aaa");
        System.out.println(cache.getIfPresent("aaa"));
        System.out.println(cache.getIfPresent("bbb"));
    }

    /**
     * 手动异步加载
     */
    public static void manualAsynLoading() throws InterruptedException {
        AsyncCache<Object, Object> asyncCache = Caffeine.newBuilder()
                .expireAfterWrite(2000, TimeUnit.MILLISECONDS)
                .buildAsync();

        //返回 key + 当前时间戳作为 value
        Function<Object, Object> getFuc = key -> key + "_" + System.currentTimeMillis();

        String key1 = "key1";

        //异步手动加载返回的不是值，而是 CompletableFuture
        CompletableFuture<Object> future = asyncCache.get(key1, getFuc);

        System.out.println("ThreadName:" + Thread.currentThread().getName()); // ThreadName:main
        //异步获取结果,对高性能场景有帮助
        future.thenAccept(o -> {
            System.out.println("ThreadName:" + Thread.currentThread().getName());
            //输出结果可以看到打印时间比生成时间晚
            System.out.println(System.currentTimeMillis() + "->" + o);
        });

        TimeUnit.MILLISECONDS.sleep(4000);

        //如果 cache 中 key 为空，直接返回 null，不为空则异步取值
        CompletableFuture<Object> ifPresent = asyncCache.getIfPresent(key1);
        if (ifPresent == null) {
            System.out.println("null");
        } else {
            //异步取值
            ifPresent.thenAccept(o -> System.out.println(o));
        }
        //批量异步取值，取不到则加载值
        CompletableFuture<Map<Object, Object>> all = asyncCache.getAll(Arrays.asList("key1", "key2", "key3"), keys -> {
            Map<Object, Object> data = new HashMap<>();
            for (Object key : keys) {
                data.put(key, key + "_all_" + System.currentTimeMillis());
            }
            return data;
        });
        //批量异步获取
        all.thenAccept(objectObjectMap -> System.out.println(objectObjectMap));

        TimeUnit.MILLISECONDS.sleep(2001);

        ConcurrentMap<Object, CompletableFuture<Object>> asMap = asyncCache.asMap();
        System.out.println(asMap);

    }

    /**
     * 自动异步加载
     */
    public static void automaticAsynLoading() throws InterruptedException {
        AsyncLoadingCache<Object, Object> asyncLoadingCache = Caffeine.newBuilder()
                //基于时间失效->写入之后开始计时失效
                .expireAfterWrite(2000, TimeUnit.MILLISECONDS)
                //同步加载和手动加载的区别就是在构建缓存时提供一个同步的加载方法
                .buildAsync(new CacheLoader<Object, Object>() {
                    //单个 key 的值加载
                    @Nullable
                    @Override
                    public Object load(@NonNull Object key) {
                        System.out.println("---exec load---");
                        return key + "_" + System.currentTimeMillis();
                    }

                    //如果没有重写 loadAll 方法则默认的 loadAll 回循环调用 load 方法，一般重写优化性能
                    @Override
                    public @NonNull
                    Map<Object, Object> loadAll(@NonNull Iterable<?> keys) {
                        System.out.println("---exec loadAll---");
                        Map<Object, Object> data = new HashMap<>();
                        for (Object key : keys) {
                            data.put(key, key + "_all_" + System.currentTimeMillis());
                        }
                        return data;
                    }
                });

        String key1 = "key1";

        //获取值，为空则执行异步加载方法
        CompletableFuture<Object> future = asyncLoadingCache.get(key1);
        //异步获取值
        future.thenAccept(o -> System.out.println(System.currentTimeMillis() + "->" + o));
        //批量异步获取
        CompletableFuture<Map<Object, Object>> all = asyncLoadingCache.getAll(Arrays.asList("key1", "key2", "key3"));
        all.thenAccept(objectObjectMap -> System.out.println(objectObjectMap));
        TimeUnit.MILLISECONDS.sleep(4000);
        ConcurrentMap<Object, CompletableFuture<Object>> asMap = asyncLoadingCache.asMap();
        System.out.println(asMap);
    }

    /**
     * 基于条数去捉
     */
    public static void evictionBasedOnNumberOfEntries() {
        Cache<Object, Object> cache = Caffeine.newBuilder()
                //缓存最大条数,超过这个条数就是驱逐缓存
                .maximumSize(20)
                .removalListener((k, v, removalCause) -> System.out.println("removed " + k + " cause " + removalCause.toString()))
                .build();

        for (int i = 0; i < 25; i++) {
            cache.put(i, i + "_value");
        }
        cache.cleanUp();
    }

    /**
     * 基于权重驱逐
     */
    public static void evictionBasedOnWeight() {
        Cache<Object, Object> cache = Caffeine.newBuilder()
                //缓存最大权重值
                .maximumWeight(150)
                //自定义计算权重
                .weigher(new Weigher<Object, Object>() {
                    @Override
                    public @NonNegative
                    int weigh(@NonNull Object k, @NonNull Object v) {
                        //这里为了简单，直接以 value 为权重
                        return (int) v;
                    }
                })
                .removalListener((k, v, removalCause) -> System.out.println("removed " + k + " cause " + removalCause.toString()))
                .build();

        for (int i = 0; i < 20; i++) {
            cache.put(i, 10);
        }
        System.out.println("before:" + cache.asMap());
        cache.cleanUp();
    }

    /**
     * 基于时间
     */
    public static void basedOnTime() throws InterruptedException {
        LoadingCache<Object, Object> cache = Caffeine.newBuilder()
                //基于时间失效->写入之后开始计时失效
                .expireAfterWrite(10, TimeUnit.SECONDS)
                //or 基于时间失效->访问之后开始计时失效
                //.expireAfterAccess(10, TimeUnit.SECONDS)
                //自定义线程池异步执行 remove 监听
                .executor(Executors.newSingleThreadExecutor())
                .removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(@Nullable Object k, @Nullable Object v, @NonNull RemovalCause removalCause) {
                        System.out.println("缓存失效了 removed " + k + " cause " + removalCause.toString());
                    }
                })
                //同步加载和手动加载的区别就是在构建缓存时提供一个同步的加载方法
                .build(new CacheLoader<Object, Object>() {
                    //单个 key 的值加载
                    @Nullable
                    @Override
                    public Object load(@NonNull Object key) throws Exception {
                        System.out.println("---exec load---");
                        return key + "_" + System.currentTimeMillis();
                    }
                });
        //放入缓存
        cache.put("k1", "v1");
        //准备失效
        TimeUnit.MILLISECONDS.sleep(15000);
        System.out.println("sleep done");
        System.out.println("我要开始取失效的缓存了");
        Object v1 = cache.get("k1");
        System.out.println(v1);
        System.exit(1);
    }


}
