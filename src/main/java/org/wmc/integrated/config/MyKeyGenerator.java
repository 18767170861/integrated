package org.wmc.integrated.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class MyKeyGenerator implements ShardingKeyGenerator {

    private AtomicLong atomic = new AtomicLong(0);

    @Override
    public Comparable<?> generateKey() {
        log.info("MyKeyGenerator .........................." + atomic.get());
        return atomic.incrementAndGet();
    }

    @Override
    public String getType() {
        //声明类型
        return "MyKeyGenerator";
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
