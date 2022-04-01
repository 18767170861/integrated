package org.wmc.integrated.stopwatch;

import org.wmc.integrated.util.JsonUtils;

import java.util.concurrent.TimeUnit;

public class TraceWatchTest {

    public static void main(String[] args) throws InterruptedException {
        TraceWatch traceWatch = new TraceWatch();

        traceWatch.start("function1");
        TimeUnit.SECONDS.sleep(1); // 模拟业务代码
        traceWatch.stop();

        traceWatch.start("function2");
        TimeUnit.SECONDS.sleep(1); // 模拟业务代码
        traceWatch.stop();

        traceWatch.record("function1", 1); // 直接记录耗时

        System.out.println(JsonUtils.serialize(traceWatch.getTaskMap()));
    }
}
