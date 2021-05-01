package com.mage.cqrs.application.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EventConfig {
    private Method method;
    private Class<?> classEvent;
}
