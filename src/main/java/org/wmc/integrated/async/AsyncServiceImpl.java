package org.wmc.integrated.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncServiceImpl implements AsyncService {

    /**
     * 若同时通过实现AsyncConfigurer配置了线程池时，如果不指定线程池，则AsyncConfigurer配置的线程池优先
     */
    @Override
    @Async("asyncServiceExecutor")
    // @Async
    public void executeAsync() {
        log.info("thread-name:{}", Thread.currentThread().getName());
        log.info("start executeAsync");
        System.out.println("异步线程要做的事情");
        System.out.println("可以在这里执行批量插入等耗时的事情");
        log.info("end executeAsync");
    }
}
