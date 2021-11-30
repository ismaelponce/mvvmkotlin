package com.example.listadofacturasmvvmkotlin.data.model;

public class FiltersWrapper {
    String desde = "";
    String hasta = "";
    int max = 0;
    boolean pagadas = false;
    boolean anuladas = false;
    boolean cuotaFija = false;
    boolean pendientesPago = false;
    boolean planPago = false;

    public FiltersWrapper(){

    }
    public FiltersWrapper(String desde,
                          String hasta,
                          int max,
                          boolean pagadas,
                          boolean anuladas,
                          boolean cuotaFija,
                          boolean pendientesPago,
                          boolean planPago
    ){
        this.desde = desde;
        this.hasta = hasta;
        this.max = max;
        this.pagadas = pagadas;
        this.anuladas = anuladas;
        this.cuotaFija = cuotaFija;
        this.pendientesPago = pendientesPago;
        this.planPago = planPago;
    }

    public String getDesde() {
        return desde;
    }

    public String getHasta() {
        return hasta;
    }

    public int getMax() {
        return max;
    }

    public boolean isPagadas() {
        return pagadas;
    }

    public boolean isAnuladas() {
        return anuladas;
    }

    public boolean isCuotaFija() {
        return cuotaFija;
    }

    public boolean isPendientesPago() {
        return pendientesPago;
    }

    public boolean isPlanPago() {
        return planPago;
    }

}
