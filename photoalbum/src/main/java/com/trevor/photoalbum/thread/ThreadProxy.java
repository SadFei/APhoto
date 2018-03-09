package com.trevor.photoalbum.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author DAIFEI
 * @date 2018/3/8
 */

public class ThreadProxy {

    private static ThreadProxy INSTANCE;

    public static ThreadProxy write() {
        if (INSTANCE == null) {
            synchronized (ThreadProxy.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ThreadProxy();
                }
            }
        }
        return INSTANCE;
    }


    private static final String THREAD_NAME = "trevor-camera";
    /**
     * 线程的核心线程数
     */
    private int corePoolSize;
    /**
     * 线程池所能容纳的最大线程数
     */
    private int maximumPoolSize;
    /**
     * 非核心线程闲置时的超时时长
     */
    private long keepAliveTime = 1L;
    /**
     * keepAliveTime 参数的时间单位
     */
    private TimeUnit unit = TimeUnit.MILLISECONDS;

    private ExecutorService singleThreadPool = null;

    private ThreadProxy() {
        // 当前设备可用处理器核心数*2 + 1,能够让cpu的效率得到最大程度执行
        corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        maximumPoolSize = corePoolSize;
    }

    public ThreadProxy corePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    public ThreadProxy maximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
        return this;
    }

    public ThreadProxy keepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    public ThreadProxy timeUnit(TimeUnit unit) {
        this.unit = unit;
        return this;
    }

    /**
     * execute
     *
     * @param runnable Runnable
     * @return ThreadProxy
     */
    public ThreadProxy execute(Runnable runnable) {

        if (singleThreadPool == null) {
            singleThreadPool = new ThreadPoolExecutor(
                    // 线程的核心线程数
                    corePoolSize,
                    // 线程池所能容纳的最大线程数
                    maximumPoolSize,
                    // 非核心线程闲置时的超时时长
                    keepAliveTime,
                    // keepAliveTime 参数的时间单位
                    unit,
                    // 线程池中的任务队列，通过线程池execute方法提交的Runnable对象会存储在这个参数中
                    new LinkedBlockingDeque<Runnable>(1024),
                    // 线程工厂，为线程池提供创建新线程的功能
                    new ThreadFactoryBuilder().setNameFormat(THREAD_NAME).build(),
                    // 用来对超出maximumPoolSize的任务的处理策略
                    new ThreadPoolExecutor.AbortPolicy());
        }

        if (runnable == null) {
            return null;
        }
        singleThreadPool.execute(runnable);
        return INSTANCE;
    }

    /**
     * shutdown
     */
    public void shutdown() {
        if (singleThreadPool == null) {
            return;
        }
        singleThreadPool.shutdown();
    }
}
