package com.example.listadofacturasmvvmkotlin.data.model;

import java.util.List;

public final class InvoiceResponseVO {
    private final double numFacturas;
    private final List<InvoiceVO> facturas;


    public final List<InvoiceVO> getFacturas() {
        return this.facturas;
    }

    public InvoiceResponseVO(double numFacturas, List<InvoiceVO> facturas) {
        this.numFacturas = numFacturas;
        this.facturas = facturas;
    }

    public String toString() {
        return "InvoiceResponse(numFacturas=" + this.numFacturas + ", facturas=" + this.facturas + ")";
    }

}


