package com.example.listadofacturasmvvmkotlin.data.repository

import android.content.Context
import com.example.listadofacturasmvvmkotlin.domain.interfaces.InvoiceRepositoryInterface
import com.example.listadofacturasmvvmkotlin.data.db.AppDatabase
import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO
import java.util.*

class InvoiceDbRepository(applicationContext: Context?) : InvoiceRepositoryInterface {
    private val appDatabase: AppDatabase = AppDatabase.getDbInstance(applicationContext)

    override suspend fun getInvoices(): List<InvoiceVO?>? {
        return Objects.requireNonNull(appDatabase.invoiceDAO().allInvoices) as List<InvoiceVO>
    }
}