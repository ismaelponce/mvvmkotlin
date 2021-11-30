package com.example.listadofacturasmvvmkotlin.data.network;


import com.example.listadofacturasmvvmkotlin.data.model.InvoiceResponseVO;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InvoiceApiClient {

    String FACTURAS_URL = "facturas";

    @GET(FACTURAS_URL)
    Call<InvoiceResponseVO> getFacturas();

}