package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @Async 默认使用的线程池：SimpleAsyncTaskExecutor:  不是真的线程池，这个类不重用线程，默认每次调用都会创建一个新的线程。
 * 也可以自定义线程池：
 * * (1)重新实现接口AsyncConfigurer
 * * (2)继承AsyncConfigurerSupport
 * * (3)配置由自定义的TaskExecutor替代内置的任务执行器
 */
@Component
public class AsyncDemo {
    private static final Logger log = LoggerFactory.getLogger(AsyncDemo.class);

    /**
     * 最简单的异步调用，返回值为void
     */
    @Async
    public void asyncInvokeSimplest() {
        log.info("线程" + Thread.currentThread().getName() + "开始执行...");
        log.info("asyncInvokeSimplest 执行结束");
    }

    /**
     * 带参数的异步调用 异步方法可以传入参数
     *
     * @param s
     */
    @Async
    public void asyncInvokeWithParameter(String s) {
        log.info("线程" + Thread.currentThread().getName() + "开始执行...");
        log.info("asyncInvokeWithParameter, parementer={}", s);
    }


    @Async
    public void asyncInvokeWithParameter(Integer s) throws InterruptedException {
        log.info("线程" + Thread.currentThread().getName() + "开始执行...");
        try {
            Thread.sleep(s*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("asyncInvokeWithParameter, parementer={}", s);
    }

    /**
     * 异常调用返回Future
     *
     * @param i
     * @return
     */
    @Async
    public Future<String> asyncInvokeReturnFuture(int i) {
        log.info("线程" + Thread.currentThread().getName() + "开始执行...");
        log.info("asyncInvokeReturnFuture, parementer={}", i);
        Future<String> future;
        try {
            Thread.sleep(1000 * 1);
            future = new AsyncResult<String>("success:" + i);
        } catch (InterruptedException e) {
            future = new AsyncResult<String>("error");
        }
        return future;
    }
}
