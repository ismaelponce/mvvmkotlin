package com.example.listadofacturasmvvmkotlin.domain.usecase;

import static com.example.listadofacturasmvvmkotlin.utils.Constants.FALLO_LLAMADA_MSSG;
import static com.example.listadofacturasmvvmkotlin.utils.Constants.FALLO_LLAMADA_TAG;

import android.util.Log;

import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO;
import com.example.listadofacturasmvvmkotlin.domain.executor.UseCaseCallback;
import com.example.listadofacturasmvvmkotlin.domain.executor.UseCaseCallbackHandler;
import com.example.listadofacturasmvvmkotlin.domain.interfaces.InvoiceRepositoryInterface;

import java.io.IOException;
import java.util.List;

public class GetInvoicesUseCase extends UseCase<List<InvoiceVO>> {
    private final InvoiceRepositoryInterface repository;

    public GetInvoicesUseCase(UseCaseCallbackHandler callbackHandler, InvoiceRepositoryInterface repositoryInterface) {
        super(callbackHandler);
        this.repository = repositoryInterface;
    }

    public void customize(UseCaseCallback<List<InvoiceVO>> callback) {
        setCallback(callback);
    }

    @Override
    public void run() {
        try {
            notifyResult(repository.getInvoices());
        } catch (IOException e) {
            Log.e(FALLO_LLAMADA_TAG, FALLO_LLAMADA_MSSG);
        }
    }
}
