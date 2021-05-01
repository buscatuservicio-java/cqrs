package com.mage.cqrs.application.config;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HandlerRoute {
    private String routeHandlers;
}
