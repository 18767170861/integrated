package org.wmc.integrated.builder;

/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: Consumer2
 * Author: 86187
 * Date: 2021/3/27 21:13
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
@FunctionalInterface
public interface Consumer2<T, P1, P2> {
    void accept(T t, P1 p1, P2 p2);
}
