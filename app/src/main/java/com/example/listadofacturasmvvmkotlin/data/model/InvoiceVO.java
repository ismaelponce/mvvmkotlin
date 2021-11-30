package com.example.listadofacturasmvvmkotlin.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "invoices")
public final class InvoiceVO {

    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") public int id = 0;
    @ColumnInfo(name = "descEstado") private final String descEstado;
    @ColumnInfo(name = "importeOrdenacion") private final double importeOrdenacion;
    @ColumnInfo(name = "fecha") private final String fecha;

    public final String getDescEstado() {
        return this.descEstado;
    }

    public final double getImporteOrdenacion() {
        return this.importeOrdenacion;
    }

    public final String getFecha() {
        return this.fecha;
    }

    public InvoiceVO(String descEstado, double importeOrdenacion, String fecha) {
        this.descEstado = descEstado;
        this.importeOrdenacion = importeOrdenacion;
        this.fecha = fecha;
    }


    public String toString() {
        return "Invoice(descEstado=" + this.descEstado + ", importeOrdenacion=" + this.importeOrdenacion + ", fecha=" + this.fecha + ")";
    }

}
