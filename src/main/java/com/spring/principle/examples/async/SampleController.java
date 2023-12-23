package com.spring.principle.examples.async;

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
    }

    @GetMapping("return-async-future")
    public void returnCompletableFuture() throws InterruptedException, ExecutionException {
        log.info("before Async");
        asyncService.completableFutureAsync()
            .thenAccept(result -> log.info("Return of Async | {}", result));
        log.info("after Async");
    }

    @GetMapping("return-async-void")
    public void returnVoid() throws InterruptedException, ExecutionException {
        log.info("{} | before Async", Thread.currentThread().getName());
        asyncService.voidAsync();
        log.info("{} | after Async", Thread.currentThread().getName());
    }

}
