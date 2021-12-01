package com.example.listadofacturasmvvmkotlin.domain.interfaces;

import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO;

import java.io.IOException;
import java.util.List;

public interface InvoiceRepositoryInterface {
    List<InvoiceVO> getInvoices() throws IOException;
}
