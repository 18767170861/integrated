package org.wmc.integrated.stopwatch;

import org.wmc.integrated.util.JsonUtils;

import java.util.concurrent.TimeUnit;

public class AutoCloseableTest {

    public static void main(String[] args) throws Exception {
        TraceWatch traceWatch = new TraceWatch();

        try (TraceWatch ignored = traceWatch.chainStart("function1")) {
            try {
                TimeUnit.SECONDS.sleep(1); // 模拟业务代码
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try (TraceWatch ignored = traceWatch.chainStart("function2")) {
            try {
                TimeUnit.SECONDS.sleep(1); // 模拟业务代码
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try (TraceWatch ignored = traceWatch.chainStart("function1")) {
            try {
                TimeUnit.SECONDS.sleep(1); // 模拟业务代码
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(JsonUtils.serialize(traceWatch.getTaskMap()));


    }

}
