package com.example.listadofacturasmvvmkotlin.presentation.listadofactura.viewmodel;

import static com.example.listadofacturasmvvmkotlin.utils.Constants.ANULADA_STRING;
import static com.example.listadofacturasmvvmkotlin.utils.Constants.CUOTA_FIJA_STRING;
import static com.example.listadofacturasmvvmkotlin.utils.Constants.LOCAL_DATE_TIME_FORMAT;
import static com.example.listadofacturasmvvmkotlin.utils.Constants.PAGADA_STRING;
import static com.example.listadofacturasmvvmkotlin.utils.Constants.PENDIENTE_PAGO_STRING;
import static com.example.listadofacturasmvvmkotlin.utils.Constants.PLAN_PAGO_STRING;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.listadofacturasmvvmkotlin.data.model.FiltersWrapper;
import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO;
import com.example.listadofacturasmvvmkotlin.data.repository.InvoiceApiRepository;
import com.example.listadofacturasmvvmkotlin.data.repository.InvoiceDbRepository;
import com.example.listadofacturasmvvmkotlin.domain.executor.UseCaseCallback;
import com.example.listadofacturasmvvmkotlin.domain.handlers.DefaultUseCaseCallbackHandler;
import com.example.listadofacturasmvvmkotlin.domain.interfaces.InvoiceRepositoryInterface;
import com.example.listadofacturasmvvmkotlin.domain.usecase.GetInvoicesUseCase;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

public class ListadoViewModel extends AndroidViewModel {

    private MutableLiveData<List<InvoiceVO>> invoices;
    private FiltersWrapper myWrapper;
    private final MutableLiveData<Boolean> _isLoading;
    private int importeMaximo;

    public ListadoViewModel(Application application){
        super(application);
        setInvoices(new MutableLiveData<>());
        _isLoading = new MutableLiveData<>();
        myWrapper = new FiltersWrapper();
        importeMaximo = 0;
    }

    public void loadInvoices() {
        GetInvoicesUseCase casoUso;

        // Se crea el caso de uso
        casoUso = new GetInvoicesUseCase(new DefaultUseCaseCallbackHandler(), getInvoiceRepository());

        // Se muestra un progress bar
        _isLoading.postValue(true);

        // Se personaliza el caso de uso
        casoUso.customize(new UseCaseCallback<List<InvoiceVO>>() {
            @Override
            public void onResult(List<InvoiceVO> result) {
                List<InvoiceVO> listaFacturas = result;
                getImporteMaximoModel(result);
                listaFacturas = filterList(listaFacturas);
                getInvoices().postValue(listaFacturas);

                // Se oculta el progress bar
                _isLoading.postValue(false);
            }
        });

        // Se ejecuta el caso de uso
        Executors.newCachedThreadPool().execute(casoUso);
    }

    private InvoiceRepositoryInterface getInvoiceRepository() {
        if (hasInternetConnection()) {
            return new InvoiceApiRepository(getApplication().getApplicationContext());
        }

        return new InvoiceDbRepository(getApplication().getApplicationContext());

    }

    private Boolean hasInternetConnection(){
        boolean connected;
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        return connected;
    }


    private List<InvoiceVO> filterList(List<InvoiceVO> listaFacturas) {
        List<InvoiceVO> filteredList = new ArrayList<>(listaFacturas);

        ArrayList<String> checks = new ArrayList<>();

        if (myWrapper.isPagadas()) {
            checks.add(PAGADA_STRING);
        }
        if (myWrapper.isPendientesPago()) {
            checks.add(PENDIENTE_PAGO_STRING);
        }
        if (myWrapper.isAnuladas()) {
            checks.add(ANULADA_STRING);
        }
        if (myWrapper.isCuotaFija()) {
            checks.add(CUOTA_FIJA_STRING);
        }
        if (myWrapper.isPlanPago()) {
            checks.add(PLAN_PAGO_STRING);
        }


        if (!myWrapper.getHasta().isEmpty() && !myWrapper.getDesde().isEmpty()) {
            Iterator<InvoiceVO> iter = filteredList.iterator();
            while (iter.hasNext()) {
                InvoiceVO invoice = iter.next();
                DateTime fechaImporte = DateTime.parse(invoice.getFecha(), DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT));
                DateTime hasta = DateTime.parse(myWrapper.getHasta(), DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT));
                DateTime desde = DateTime.parse(myWrapper.getDesde(), DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT));


                if (fechaImporte.isAfter(hasta) || fechaImporte.isBefore(desde)) {
                    iter.remove();
                }
            }
        } else if (!myWrapper.getHasta().isEmpty()) {
            Iterator<InvoiceVO> iter = filteredList.iterator();
            while (iter.hasNext()) {
                InvoiceVO invoice = iter.next();
                DateTime fechaImporte = DateTime.parse(invoice.getFecha(), DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT));
                DateTime hasta = DateTime.parse(myWrapper.getHasta(), DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT));

                if (fechaImporte.isAfter(hasta)) {
                    iter.remove();
                }
            }
        } else if (!myWrapper.getDesde().isEmpty()) {
            Iterator<InvoiceVO> iter = filteredList.iterator();

            while (iter.hasNext()) {
                InvoiceVO invoice = iter.next();
                DateTime fechaImporte = DateTime.parse(invoice.getFecha(), DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT));
                DateTime desde = DateTime.parse(myWrapper.getDesde(), DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT));

                if (fechaImporte.isBefore(desde)) {
                    iter.remove();
                }
            }

        }

        if (myWrapper.getMax() > 0) {
            Iterator<InvoiceVO> iter = filteredList.iterator();
            while (iter.hasNext()) {
                InvoiceVO invoice = iter.next();

                if (invoice.getImporteOrdenacion() > myWrapper.getMax()) {
                    iter.remove();
                }
            }
        }

        if (checks.size() > 0) {
            Iterator<InvoiceVO> iter = filteredList.iterator();
            while (iter.hasNext()) {
                InvoiceVO invoice = iter.next();

                if (!checks.contains(invoice.getDescEstado())) {
                    iter.remove();
                }
            }
        }

        return  filteredList;
    }

    public void setWrapper(FiltersWrapper myWrapper) {
        this.myWrapper = myWrapper;
    }

    private void getImporteMaximoModel(List<InvoiceVO> listaFacturas) {
        int max = 0;

        for (InvoiceVO i: listaFacturas) {
            if (i.getImporteOrdenacion() >= max) {
                max = (int) i.getImporteOrdenacion();
            }
        }

        this.importeMaximo = max;
    }

    public int getImporteMaximo() {
        return this.importeMaximo;
    }


    public MutableLiveData<List<InvoiceVO>> getInvoices(){
        return this.invoices;
    }

    private void setInvoices(MutableLiveData<List<InvoiceVO>> invoices) {
        this.invoices = invoices;
    }

    public LiveData<Boolean> isLoading(){
        return this._isLoading;
    }

}
