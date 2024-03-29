package org.wmc.integrated.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
@EnableAsync
public class ExecutorConfig {

    /**
     * # 异步线程配置
     * # 配置核心线程数
     * async.executor.thread.core_pool_size = 5
     * # 配置最大线程数
     * async.executor.thread.max_pool_size = 5
     * # 配置队列大小
     * async.executor.thread.queue_capacity = 99999
     * # 配置线程池中的线程的名称前缀
     * async.executor.thread.name.prefix = async-service-
     *
     */
    //@Value("${async.executor.thread.core_pool_size}")
    private int corePoolSize = 10;
    //@Value("${async.executor.thread.max_pool_size}")
    private int maxPoolSize = 20;
    //@Value("${async.executor.thread.queue_capacity}")
    private int queueCapacity = 1000;
    //@Value("${async.executor.thread.name.prefix}")
    private String namePrefix = "async-service-";

//    @Bean(name = "asyncServiceExecutor")
//    public Executor asyncServiceExecutor() {
//        log.info("start asyncServiceExecutor");
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        //配置核心线程数
//        executor.setCorePoolSize(corePoolSize);
//        //配置最大线程数
//        executor.setMaxPoolSize(maxPoolSize);
//        //配置队列大小
//        executor.setQueueCapacity(queueCapacity);
//        //配置线程池中的线程的名称前缀
//        executor.setThreadNamePrefix(namePrefix);
//
//        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
//        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        //执行初始化
//        executor.initialize();
//        return executor;
//    }

    @Bean(name = "asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        log.info("start asyncServiceExecutor");
        //在这里修改
        ThreadPoolTaskExecutor executor = new VisiableThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(corePoolSize);
        //配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(queueCapacity);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(namePrefix);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}