package com.spring.principle.examples.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class SampleController {

    private final AsyncService asyncService;

    @GetMapping("return-sync-void")
    public void returnSyncVoid() throws InterruptedException {
        log.info("{} | before void", Thread.currentThread().getName());
        asyncService.voidSync();
        log.info("{} | after void", Thread.currentThread().getName());
        return;
    }

    @GetMapping("return-async-future")
    public Object returnFuture() throws InterruptedException, ExecutionException {
        log.info("{} | before Async", Thread.currentThread().getName());
        CompletableFuture<Integer> future = asyncService.completableFutureAsync();
        log.info("{} | after Async", Thread.currentThread().getName());
        log.info("{} | future.get() | {}", Thread.currentThread().getName(), future.get());
        return future.get();
    }

    @GetMapping("return-async-void")
    public void returnVoid() throws InterruptedException, ExecutionException {
        log.info("{} | before Async", Thread.currentThread().getName());
        asyncService.voidAsync();
        log.info("{} | after Async", Thread.currentThread().getName());
        return;
    }

}
