package com.genersoft.iot.vmp.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
// 强制使用CGLIB类代理。部分Service同时实现接口并声明了不在接口中的@Scheduled方法；
// 使用JDK接口代理会导致Spring无法调用这些定时方法，应用启动失败。
@EnableAsync(proxyTargetClass = true)
public class AsyncConfig implements AsyncConfigurer {

    private static final int QUEUE_CAPACITY = 10_000;

    @Bean(name = {"applicationTaskExecutor", "taskExecutor"})
    public TaskExecutor applicationTaskExecutor() {
        int corePoolSize = Math.max(4, Runtime.getRuntime().availableProcessors());
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(corePoolSize * 4);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("wvp-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return applicationTaskExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, parameters) ->
                log.error("异步任务执行失败, method={}", method.getName(), throwable);
    }
}
