package org.wmc.integrated.healthChecker;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * 抽象出所有健康检查行为
 * */
@Getter
public abstract class BaseHealthChecker implements Runnable {
    /*
     * 服务名称
     * */
    private String serviceName;
    /*
     * 是否健康
     * */
    private Boolean serviceUp;
    /*
     * 闭锁（控制每次所有的服务检查一致性）
     * */
    private CountDownLatch latch;

    public BaseHealthChecker(String serviceName, CountDownLatch latch) {
        super();
        this.latch = latch;
        this.serviceName = serviceName;
        this.serviceUp = false;
    }

    @Override
    public void run() {
        try {
            verifyService();
            serviceUp = true;
        } catch (Exception e) {
            e.printStackTrace();
            serviceUp = false;
        } finally {
            if (Objects.nonNull(latch)) {
                latch.countDown();
            }
        }
    }

    /**
     * 服务检查，各调用者只需要去实现这个行为即可（只要没有报错 就认为是健康的）
     */
    protected abstract void verifyService();
}

/**
 * 检查DB数据库
 */
class DBHealthChecker extends BaseHealthChecker {
    public DBHealthChecker(CountDownLatch latch) {
        super("Network Service", latch);
    }

    //模拟数据库 只要不抛出异常 就认为是up的
    @Override
    public void verifyService() {
        System.out.println("Checking " + this.getServiceName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getServiceName() + " is UP");
    }
}

/**
 * 检查网络
 */
class NetworkHealthChecker extends BaseHealthChecker {
    public NetworkHealthChecker(CountDownLatch latch) {
        super("Network Service", latch);
    }

    //模拟网络检查 只要不抛出异常 就认为是up的
    @Override
    public void verifyService() {
        System.out.println("Checking " + this.getServiceName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getServiceName() + " is UP");
    }
}

class Test {
    // 使用main方法代替定时任务 检测
    public static void main(String[] args) throws InterruptedException {
        // 需要检查的服务
        List<BaseHealthChecker> listChecker = new ArrayList<>();
        // 根据服务数量创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        // 根据服务数量确定闭锁数量
        CountDownLatch latch = new CountDownLatch(2);
        listChecker.add(new NetworkHealthChecker(latch));
        listChecker.add(new DBHealthChecker(latch));
        // 线程池执行服务检查
        listChecker.forEach(executorService::submit);
        // 等所有检查结束
        latch.await();
        // 打印服务状态
        listChecker.forEach(checker -> System.out.println(checker.getServiceName() + "服务的up状态为" + checker.getServiceUp()));
    }
}