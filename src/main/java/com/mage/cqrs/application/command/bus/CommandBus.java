package com.mage.cqrs.application.command.bus;

import com.mage.cqrs.domain.Command;
import reactor.core.publisher.Mono;

public interface CommandBus {
    <R, C extends Command> Mono<R> execute(C command, Class<? extends Command> type);
}
