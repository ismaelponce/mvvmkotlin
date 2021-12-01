package com.example.listadofacturasmvvmkotlin.data.network

import com.example.listadofacturasmvvmkotlin.data.network.RetrofitHelper
import kotlin.Throws
import com.example.listadofacturasmvvmkotlin.data.model.InvoiceResponseVO
import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO
import com.example.listadofacturasmvvmkotlin.data.network.InvoiceApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class InvoiceService {
    private val retrofit: RetrofitHelper = RetrofitHelper()


    suspend fun getInvoices(): List<InvoiceVO>{
        return withContext(Dispatchers.IO){
            val apiService = retrofit.retrofit.create(InvoiceApiClient::class.java)
            val response: Response<InvoiceResponseVO> = apiService.facturas.execute()
            response.body()?.facturas ?: emptyList()
        }

    }


}