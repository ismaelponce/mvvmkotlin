package com.example.listadofacturasmvvmkotlin.domain.interfaces

import kotlin.Throws
import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO
import java.io.IOException

interface InvoiceRepositoryInterface {
    suspend fun getInvoices(): List<InvoiceVO?>?
}