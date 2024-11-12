package com.spring.app.urlshorter.util;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class RepositoryHelper {
    @Transactional
    public void runInTransaction(Runnable tx) {
        tx.run();
    }

    @Transactional
    public <T> T runInTransaction(Supplier<T> tx) {
        return tx.get();
    }
}
