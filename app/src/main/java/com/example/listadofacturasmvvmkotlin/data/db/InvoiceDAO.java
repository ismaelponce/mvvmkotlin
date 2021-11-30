package com.example.listadofacturasmvvmkotlin.data.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO;

import java.util.List;

@Dao
public interface InvoiceDAO {

    @Query("SELECT * FROM invoices")
    List<InvoiceVO> getAllInvoices();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertInvoice(InvoiceVO invoiceVO);

    @Query("DELETE FROM invoices")
    void deleteInvoices();
}
