package com.example.listadofacturasmvvmkotlin.data.repository;

import android.content.Context;

import com.example.listadofacturasmvvmkotlin.data.db.AppDatabase;
import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO;
import com.example.listadofacturasmvvmkotlin.domain.interfaces.InvoiceRepositoryInterface;

import java.util.List;
import java.util.Objects;

public class InvoiceDbRepository implements InvoiceRepositoryInterface {
    private final AppDatabase appDatabase;
    public InvoiceDbRepository(Context applicationContext) {
        this.appDatabase = AppDatabase.getDbInstance(applicationContext);
    }

    @Override
    public List<InvoiceVO> getInvoices() {
        return (List<InvoiceVO>) Objects.requireNonNull(appDatabase.invoiceDAO().getAllInvoices());
    }
}
