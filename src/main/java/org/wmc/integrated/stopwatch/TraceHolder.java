package org.wmc.integrated.stopwatch;

import java.util.function.IntConsumer;
import java.util.function.Supplier;

public class TraceHolder {

    /**
     * 有返回值调用
     */
    public static <T> T run(TraceWatch traceWatch, String taskName, Supplier<T> supplier) {
        try {
            traceWatch.start(taskName);
            return supplier.get();
        } finally {
            traceWatch.stop();
        }
    }

    /**
     * 无返回值调用
     */
    public static void run(TraceWatch traceWatch, String taskName, IntConsumer function) {
        try {
            traceWatch.start(taskName);
            function.accept(0);
        } finally {
            traceWatch.stop();
        }
    }

}
