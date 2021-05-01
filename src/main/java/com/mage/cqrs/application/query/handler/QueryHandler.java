package com.mage.cqrs.application.query.handler;

import com.mage.cqrs.domain.Query;

public interface QueryHandler<Q extends Query, R> {

    R handle(Q request);
}
