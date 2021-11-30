package com.example.listadofacturasmvvmkotlin.domain.executor;

public interface UseCaseCallbackHandler {
    void post(Runnable runnable);
}
