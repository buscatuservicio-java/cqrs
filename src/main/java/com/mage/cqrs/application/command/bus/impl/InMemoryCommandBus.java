package com.mage.cqrs.application.command.bus.impl;


import com.mage.cqrs.application.command.bus.CommandBus;
import com.mage.cqrs.application.config.ScannerHandler;
import com.mage.cqrs.domain.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;

@Slf4j
@Service
public class InMemoryCommandBus implements CommandBus {
    private final ScannerHandler scannerHandler;
    private final ApplicationContext applicationContext;

    public InMemoryCommandBus(ScannerHandler scannerHandler, ApplicationContext applicationContext) {
        this.scannerHandler = scannerHandler;
        this.applicationContext = applicationContext;
    }

    @Override
    public <R, C extends Command> Mono<R> execute(Mono<C> command, Class<? extends Command> type) {
        try {
            Class<?> handler = scannerHandler.getMapCommandHandlers().get(type.getTypeName());
            return (Mono<R>) handler.getDeclaredMethods()[0].invoke(applicationContext.getBean(handler), command);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
