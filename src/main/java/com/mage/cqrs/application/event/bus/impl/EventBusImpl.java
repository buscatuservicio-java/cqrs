package com.mage.cqrs.application.event.bus.impl;

import com.mage.cqrs.application.config.EventConfig;
import com.mage.cqrs.application.config.ScannerHandler;
import com.mage.cqrs.application.event.bus.EventBus;
import com.mage.cqrs.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class EventBusImpl implements EventBus {
    private final ScannerHandler scannerHandler;
    private final ApplicationContext applicationContext;

    public EventBusImpl(ScannerHandler scannerHandler, ApplicationContext applicationContext) {
        this.scannerHandler = scannerHandler;
        this.applicationContext = applicationContext;
    }

    @Override
    public void publish(Mono<Event> event) {
        final CompletableFuture<Object> completableFuture = new CompletableFuture<>();
        List<EventConfig> eventsConfig = scannerHandler.getMapEventHandlers().get(event.getClass().getTypeName());
        eventsConfig.parallelStream().forEach(eventConfig -> {
            Executors.newCachedThreadPool().submit(() -> {
                try {
                    completableFuture.complete(eventConfig.getMethod().invoke(applicationContext.getBean(eventConfig.getClassEvent()), event));
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    ex.printStackTrace();
                }
            });
        });
    }
}