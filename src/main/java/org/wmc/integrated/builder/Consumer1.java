package org.wmc.integrated.builder;

@FunctionalInterface
public interface Consumer1<T, P1> {
    void accept(T t, P1 p1);
}