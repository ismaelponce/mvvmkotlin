package com.example.listadofacturasmvvmkotlin.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO;

@Database(entities = {InvoiceVO.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "INVOICE_DB";

    public abstract InvoiceDAO invoiceDAO();

    private static AppDatabase INSTANCE;


    public static AppDatabase getDbInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

}
