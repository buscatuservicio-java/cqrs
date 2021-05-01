package com.mage.cqrs.application.query.bus;


import com.mage.cqrs.domain.Query;
import reactor.core.publisher.Mono;

public interface QueryBus {
    <R, Q extends Query> Mono<R> ask(Q query);
}
