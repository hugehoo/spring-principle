package com.spring.principle.examples.async;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AsyncService {

    public void voidSync() throws InterruptedException {
        log.info("{} | voidSync()", Thread.currentThread().getName());
        Thread.sleep(500);
        log.info("{} | End of Sleep", Thread.currentThread().getName());
    }

    @Async
    public void voidAsync() throws InterruptedException {
        log.info("{} | voidAsync()", Thread.currentThread().getName());
        Thread.sleep(500);
        log.info("{} | End of Sleep", Thread.currentThread().getName());
    }

    @Async
    public CompletableFuture<Integer> completableFutureAsync() throws InterruptedException {
        log.info("middle of completableFutureAsync()");
        return new AsyncResult<>(1).completable();
    }
}
