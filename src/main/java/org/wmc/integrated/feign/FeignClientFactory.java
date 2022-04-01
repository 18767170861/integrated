package org.wmc.integrated.feign;

import feign.Feign;
import feign.Logger;
import feign.Retryer;

public abstract class FeignClientFactory {

    /**
     * feign.Logger.JavaLogger：使用的java.util.logging.Logger输出，但是日志级别的FINE级别，默认不会输出到控制台
     * feign.Logger.ErrorLogger：错误输出。使用的System.err.printf()输出
     * feign.Logger.NoOpLogger：什么都不输出，它是Feign的默认使用的Logger实现，也就是不会给控制台输出
     * 鉴于此，为了在控制台看到效果，因此本例（下同）所有的Logger实现均采用ErrorLogger~，并且不开启重试，统一由如下工厂创建出Client实例：
     *
     * @param cl
     * @param <T>
     * @return
     */
    static <T> T create(Class<T> cl) {
        return Feign.builder()
                .logger(new Logger.ErrorLogger()).logLevel(Logger.Level.FULL) // 输出日志到控制台
                .retryer(Retryer.NEVER_RETRY) // 关闭重试
                .decode404() // 把404也解码 -> 这样就不会以一场形式抛出，中断程序喽，方便我测试嘛
                .target(cl, "http://localhost:8081");
    }

}
