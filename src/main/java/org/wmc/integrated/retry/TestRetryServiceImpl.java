package org.wmc.integrated.retry;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class TestRetryServiceImpl implements TestRetryService {

    /**
     * @Retryable ： 注解方式标记当前方法会使用重试机制
     * 里面的 value： 重试的触发机制，当遇到Exception异常的时候，触发；
     * maxAttempts： 重试的次数（包括第一次调用，也就是说如果设置3次，调用一次后，如果一直失败触发重试，那么还当前方法还会调用2次）；
     * delay：重试的延迟时间，也就是距离上一次重试方法调用的间隔，单位毫秒
     * multiplier： delay间隔时间的倍数，也就是说，第一次重试间隔如果是2000ms，那第二次重试的时候就是2000ms 乘以这个倍数1.5，就是3000ms；
     * maxDelay:重试次数之间的最大时间间隔,默认为0,即忽略,如果小于delay的设置,则默认为30000L；
     */
    @Override
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public int dignifiedTest(int code) throws Exception {
        System.out.println("dignifiedTest被调用,时间：" + LocalTime.now());
        if (code == 0) {
            throw new Exception("情况不对头！");
        }
        System.out.println("dignifiedTest被调用,情况对头了！");

        return 200;
    }

    /**
     * 这个方法用到了@Recover，也就是用注解方式标记当期方法为回调方法，可以看到传参里面写的是 Exception e，这个是作为回调的接头暗号（重试次数用完了，还是失败，我们抛出这个Exception e通知触发这个回调方法）。
     * <p>
     * PS：该回调方法与重试方法写在同一个实现类里面。
     */
    @Recover
    public int recover(Exception e) {
        System.out.println("回调方法执行！！！！");
        //记日志到数据库 或者调用其余的方法
        return 400;
    }

}
