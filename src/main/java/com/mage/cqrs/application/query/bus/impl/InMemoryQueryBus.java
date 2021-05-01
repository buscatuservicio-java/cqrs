package com.mage.cqrs.application.query.bus.impl;

import com.mage.cqrs.application.config.ScannerHandler;
import com.mage.cqrs.application.query.bus.QueryBus;
import com.mage.cqrs.domain.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class InMemoryQueryBus implements QueryBus {
    private final ScannerHandler scannerHandler;
    private final ApplicationContext applicationContext;

    public InMemoryQueryBus(ScannerHandler scannerHandler, ApplicationContext applicationContext) {
        this.scannerHandler = scannerHandler;
        this.applicationContext = applicationContext;
    }

    @Override
    public <R, Q extends Query> Mono<R> ask(Q query) {
        Mono<R> result = null;
        try {
            Class<?> handler = scannerHandler.getMapQueryHandlers().get(query.getClass().getTypeName());
            result = (Mono<R>) handler.getDeclaredMethods()[0].invoke(applicationContext.getBean(handler), query);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return result;
    }


}
