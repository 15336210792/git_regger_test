package com.zzh.common;

/**
 * 基于ThreadLocal线程封装工具类，用户保存和获取当前登录用户id
 * 作用域为同一线程
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
       return threadLocal.get();
    }
}
