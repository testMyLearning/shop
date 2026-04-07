package com.oz.common.executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
@EnableAsync
@Configuration
public class CustomThreadPoolExecutor {


    @Bean(name= "customExecutor")
    public Executor customExecutor(){
        int cores = Runtime.getRuntime().availableProcessors();
        int corePoolSize = cores * 2;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
executor.setQueueCapacity(500);
executor.setCorePoolSize(corePoolSize);
executor.setMaxPoolSize(Math.max(corePoolSize,20));
executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
executor.setThreadNamePrefix("Custom-");
        executor.setWaitForTasksToCompleteOnShutdown(true); // ждать завершения
        executor.setAwaitTerminationSeconds(30);
executor.initialize();
return executor;
    }
}
