package org.wmc.integrated.shutdown;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ShutdownGraceFullTest {
    /**
     * 使用线程池处理任务
     */
    public static ExecutorService executorService = Executors.newCachedThreadPool();
    public static void main(String [] args) {
        //假设有5个线程需要执行任务
        for(int i = 0; i < 5; i++){
            final int id = i;
            Thread taski = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(System.currentTimeMillis() + " : thread_" + id + " start...");
                    try{
                        TimeUnit.SECONDS.sleep(id);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(System.currentTimeMillis() + " : thread_" + id + " finish!");
                }
            });
            taski.setDaemon(true);
            executorService.submit(taski);
        }

        // 添加一个钩子处理未完任务
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " No1 shutdown hooking...");
                try {
                    executorService.shutdown();
                    System.out.println(System.currentTimeMillis() + " : " + Thread .currentThread().getName() + " shutdown signal got, wait threadPool finish."
                    );
                    executorService.awaitTermination(1500, TimeUnit.SECONDS);
                    System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " all thread's done.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " No1 shutdown done...");
            }
        }));
        // 多个关闭钩子并发执行
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " No2 shutdown hooking...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " No2 shutdown done...");
        }));
        System.out.println("main method exit...");
        // 故意调用jvm退出命令，发送关闭信号，否则正常情况下 jvm 会等待最后一个非守护线程关闭才会退出
        System.exit(0);
    }

}