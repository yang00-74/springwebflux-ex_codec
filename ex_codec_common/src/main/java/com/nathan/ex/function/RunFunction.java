package com.nathan.ex.function;

/**
 * @author nathan.yang
 * @date 2019/12/1
 */
@FunctionalInterface
public interface RunFunction<R> {

    R run(String t);

    default void walk(){ System.out.println("Walk slow");}

}
