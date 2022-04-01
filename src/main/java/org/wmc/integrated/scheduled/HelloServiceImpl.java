package org.wmc.integrated.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Slf4j
@Service
public class HelloServiceImpl implements HelloService {
    // @Schedules
    // 它是允许重复注解的~~~~
    //@Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "0/2 * * * * ?") // 每2秒钟执行一次
    public void job1() {
        log.info("threan name:{}", Thread.currentThread().getName());
        System.out.println("我执行了~~" + LocalTime.now());
    }
}