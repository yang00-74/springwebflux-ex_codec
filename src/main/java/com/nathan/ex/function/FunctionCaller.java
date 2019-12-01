package com.nathan.ex.function;

/**
 * @author nathan.yang
 * @date 2019/12/1
 */
public class FunctionCaller {

    public static void call(RunFunction runFunction) {
        String s = runFunction.run("Hello:").toString();
        runFunction.walk();
        System.out.println(s);
    }

}
