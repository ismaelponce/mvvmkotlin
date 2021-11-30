package com.example.listadofacturasmvvmkotlin.domain.executor;

public interface UseCaseCallback <R>{
    void onResult(R result);
}
