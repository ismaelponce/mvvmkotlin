package com.example.listadofacturasmvvmkotlin.data.repository;

import android.content.Context;

import com.example.listadofacturasmvvmkotlin.data.db.AppDatabase;
import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO;
import com.example.listadofacturasmvvmkotlin.data.network.InvoiceService;
import com.example.listadofacturasmvvmkotlin.domain.interfaces.InvoiceRepositoryInterface;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class InvoiceApiRepository implements InvoiceRepositoryInterface {
    private final InvoiceService api;
    private final AppDatabase appDatabase;

    public InvoiceApiRepository(Context applicationContext) {
        this.api = new InvoiceService();
        this.appDatabase = AppDatabase.getDbInstance(applicationContext);
    }

    @Override
    public List<InvoiceVO> getInvoices() throws IOException {
        appDatabase.invoiceDAO().deleteInvoices();
        List<InvoiceVO> list = Objects.requireNonNull(api.getInvoices().body()).getFacturas();
        for (InvoiceVO invoice:
             list) {
            appDatabase.invoiceDAO().insertInvoice(invoice);
        }
        return list;
    }
}
