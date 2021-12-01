package com.example.listadofacturasmvvmkotlin.data.repository

import android.content.Context
import com.example.listadofacturasmvvmkotlin.domain.interfaces.InvoiceRepositoryInterface
import com.example.listadofacturasmvvmkotlin.data.network.InvoiceService
import com.example.listadofacturasmvvmkotlin.data.db.AppDatabase
import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO
import java.util.*

class InvoiceApiRepository(applicationContext: Context?) : InvoiceRepositoryInterface {
    private val api: InvoiceService = InvoiceService()
    private val appDatabase: AppDatabase = AppDatabase.getDbInstance(applicationContext)
    override suspend fun getInvoices(): List<InvoiceVO>{
        appDatabase.invoiceDAO().deleteInvoices()
        val list = api.getInvoices()
        for (invoice in list) {
            appDatabase.invoiceDAO().insertInvoice(invoice)
        }
        return list
    }


}