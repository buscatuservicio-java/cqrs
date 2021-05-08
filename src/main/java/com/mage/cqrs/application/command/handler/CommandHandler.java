package com.mage.cqrs.application.command.handler;

import com.mage.cqrs.domain.Command;
import reactor.core.publisher.Mono;


public interface CommandHandler<C extends Command, R> {
    Mono<R> handle(Mono<C> command);
}
