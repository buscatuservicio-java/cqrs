package com.mage.cqrs.application.event.bus;

import com.mage.cqrs.domain.Event;
import reactor.core.publisher.Mono;

public interface EventBus {
    void publish(Mono<Event> event);
}
