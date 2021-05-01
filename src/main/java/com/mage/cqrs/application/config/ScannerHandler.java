package com.mage.cqrs.application.config;


import com.mage.cqrs.application.command.handler.CommandHandler;
import com.mage.cqrs.application.event.handler.EventHandler;
import com.mage.cqrs.application.query.handler.QueryHandler;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@Configuration
@Slf4j
public class ScannerHandler {
    private final HandlerRoute handlerRoute;
    private Map<String, Class<?>> mapCommandHandlers = new HashMap<>();
    private Map<String, Class<?>> mapQueryHandlers = new HashMap<>();
    private Map<String, List<EventConfig>> mapEventHandlers = new HashMap<>();

    public ScannerHandler(HandlerRoute handlerRoute) {
        this.handlerRoute = handlerRoute;
    }

    @Bean
    public ScannerHandler scanHandlers() {
        Reflections reflections = new Reflections(handlerRoute.getRouteHandlers());
        loadMapHandler(CommandHandler.class, mapCommandHandlers, reflections);
        loadMapHandler(QueryHandler.class, mapQueryHandlers, reflections);
        loadMapEventHandler();
        return this;
    }

    private void loadMapHandler(Class<?> interfaceHandler, Map<String, Class<?>> map, Reflections reflections) {
        reflections.getSubTypesOf(interfaceHandler).forEach(aClass -> {
            Type genericTypes = ((ParameterizedType) aClass.getGenericInterfaces()[0]).getActualTypeArguments()[0];
            map.put(genericTypes.getTypeName(), aClass);
        });
    }

    private void loadMapEventHandler() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(handlerRoute.getRouteHandlers()))
                .setScanners(new MethodAnnotationsScanner()));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(EventHandler.class);
        methods.forEach(method -> {
            String eventName = method.getAnnotation(EventHandler.class).value().getName();
            EventConfig eventConfig = new EventConfig(method, method.getDeclaringClass());
            List<EventConfig> eventsConfig = null;
            if (mapEventHandlers.containsKey(eventName)) {
                eventsConfig = mapEventHandlers.get(eventName);
            } else {
                eventsConfig = new ArrayList();
            }
            eventsConfig.add(eventConfig);
            mapEventHandlers.put(eventName, eventsConfig);
        });
    }

    public Map<String, Class<?>> getMapCommandHandlers() {
        return mapCommandHandlers;
    }

    public Map<String, Class<?>> getMapQueryHandlers() {
        return mapQueryHandlers;
    }

    public Map<String, List<EventConfig>> getMapEventHandlers() {
        return mapEventHandlers;
    }
}
