package com.example.listadofacturasmvvmkotlin.domain.usecase

import android.util.Log
import com.example.listadofacturasmvvmkotlin.domain.executor.UseCaseCallbackHandler
import com.example.listadofacturasmvvmkotlin.domain.interfaces.InvoiceRepositoryInterface
import com.example.listadofacturasmvvmkotlin.domain.executor.UseCaseCallback
import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO
import java.io.IOException

class GetInvoicesUseCase(repositoryInterface: InvoiceRepositoryInterface) {
    private val repository: InvoiceRepositoryInterface = repositoryInterface

    suspend operator fun invoke(): List<InvoiceVO?>? = repository.getInvoices()
}