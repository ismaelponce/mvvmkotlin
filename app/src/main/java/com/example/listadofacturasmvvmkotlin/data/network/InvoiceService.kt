package com.example.listadofacturasmvvmkotlin.data.network;

import com.example.listadofacturasmvvmkotlin.data.model.InvoiceResponseVO;

import java.io.IOException;

import retrofit2.Response;

public class InvoiceService {

    private final RetrofitHelper retrofit;

    public InvoiceService(){
        retrofit = new RetrofitHelper();
    }

    public Response<InvoiceResponseVO> getInvoices() throws IOException {
        InvoiceApiClient apiService = retrofit.getRetrofit().create(InvoiceApiClient.class);

        Response<InvoiceResponseVO> response;
        response = apiService.getFacturas().execute();
        return response;
    }
}
