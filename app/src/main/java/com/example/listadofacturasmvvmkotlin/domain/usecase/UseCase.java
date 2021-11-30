package com.example.listadofacturasmvvmkotlin.domain.usecase;

import com.example.listadofacturasmvvmkotlin.domain.executor.UseCaseCallback;
import com.example.listadofacturasmvvmkotlin.domain.executor.UseCaseCallbackHandler;

public abstract class UseCase<R> implements Runnable {
    private final UseCaseCallbackHandler callbackHandler;
    private UseCaseCallback<R> callback;

    public UseCase(UseCaseCallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    public void notifyResult(final R result) {
        callbackHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResult(result);
                }
            }
        });
    }

    public void setCallback(UseCaseCallback<R> callback) {
        this.callback = callback;
    }

}