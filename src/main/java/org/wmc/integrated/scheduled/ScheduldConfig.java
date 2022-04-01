package org.wmc.integrated.scheduled;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@EnableScheduling
@Configuration
public class ScheduldConfig  implements SchedulingConfigurer {

    /**
     * 通过配置文件配置
     * # 线程池大小
     * spring.task.scheduling.pool.size=10
     * # 线程名前缀
     * spring.task.scheduling.thread-name-prefix=task-pool-
     *
     */
    // 下面是推荐的配置，当然你也可以简单粗暴的使用它： 开启100个核心线程 足够用了吧
    // taskRegistrar.setScheduler(Executors.newScheduledThreadPool(100));
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 方式一：可议直接使用一个TaskScheduler 然后设置上poolSize等参数即可 （推荐）
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        //taskScheduler.setPoolSize(10);
        //taskScheduler.initialize(); // 记得调用啊
        //taskRegistrar.setTaskScheduler(taskScheduler);

        // 方式二：ScheduledExecutorService(使用ScheduledThreadPoolExecutor,它继承自JUC的ThreadPoolExecutor，是一个和任务调度相关的线程池)
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(10);
        poolExecutor.setThreadFactory(r -> {
            AtomicInteger atoInteger = new AtomicInteger(0);
            Thread t = new Thread(r);
            t.setName("Scheduled-Thread- " + atoInteger.getAndIncrement());
            return t;
        });
        taskRegistrar.setScheduler(poolExecutor);

        // =========示例：这里，我们也可以注册一个任务，一直执行的~~~=========
        taskRegistrar.addFixedRateTask(() -> System.out.println("执行定时任务1: " + new Date()), 1000);

    }
}
