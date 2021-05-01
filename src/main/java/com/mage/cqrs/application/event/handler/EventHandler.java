package com.mage.cqrs.application.event.handler;

import com.mage.cqrs.domain.Event;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventHandler {
    Class<? extends Event> value();
}
