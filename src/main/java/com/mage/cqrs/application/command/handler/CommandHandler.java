package com.mage.cqrs.application.command.handler;

import com.mage.cqrs.domain.Command;

public interface CommandHandler<C extends Command, R> {
    R handle(C command);
}
