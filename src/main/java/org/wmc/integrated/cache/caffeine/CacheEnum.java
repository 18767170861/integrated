package org.wmc.integrated.cache.caffeine;

import lombok.Data;
import lombok.Getter;

@Getter
public enum CacheEnum {
    /**
     * 第一个cache
     **/
    FIRST_CACHE(300, 20000, 300),
    /**
     * 第二个cache
     **/
    SECOND_CACHE(60, 10000, 200);

    private int second;
    private long maxSize;
    private int initSize;

    CacheEnum(int second, long maxSize, int initSize) {
        this.second = second;
        this.maxSize = maxSize;
        this.initSize = initSize;
    }

}