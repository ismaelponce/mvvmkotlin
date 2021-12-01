package com.example.listadofacturasmvvmkotlin.presentation.listadofactura.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO
import com.example.listadofacturasmvvmkotlin.data.model.FiltersWrapper
import com.example.listadofacturasmvvmkotlin.domain.usecase.GetInvoicesUseCase
import com.example.listadofacturasmvvmkotlin.domain.handlers.DefaultUseCaseCallbackHandler
import com.example.listadofacturasmvvmkotlin.domain.executor.UseCaseCallback
import com.example.listadofacturasmvvmkotlin.domain.interfaces.InvoiceRepositoryInterface
import com.example.listadofacturasmvvmkotlin.data.repository.InvoiceApiRepository
import com.example.listadofacturasmvvmkotlin.data.repository.InvoiceDbRepository
import android.net.ConnectivityManager
import android.net.NetworkInfo
import org.joda.time.format.DateTimeFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.listadofacturasmvvmkotlin.utils.Constants.ANULADA_STRING
import com.example.listadofacturasmvvmkotlin.utils.Constants.CUOTA_FIJA_STRING
import com.example.listadofacturasmvvmkotlin.utils.Constants.LOCAL_DATE_TIME_FORMAT
import com.example.listadofacturasmvvmkotlin.utils.Constants.PAGADA_STRING
import com.example.listadofacturasmvvmkotlin.utils.Constants.PENDIENTE_PAGO_STRING
import com.example.listadofacturasmvvmkotlin.utils.Constants.PLAN_PAGO_STRING
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.ArrayList
import java.util.concurrent.Executors

class ListadoViewModel(application: Application?) : AndroidViewModel(application!!) {
    var invoices: MutableLiveData<List<InvoiceVO>>? = null
        private set
    private var myWrapper: FiltersWrapper
    private val _isLoading: MutableLiveData<Boolean>
    var importeMaximo: Int
        private set

    fun loadInvoices() {


        val casoUso: GetInvoicesUseCase

        // Se crea el caso de uso
        casoUso = GetInvoicesUseCase(DefaultUseCaseCallbackHandler(), invoiceRepository)

        // Se muestra un progress bar
        _isLoading.postValue(true)

        // Se personaliza el caso de uso
        casoUso.customize(object : UseCaseCallback<List<InvoiceVO?>?> {
            override fun onResult(result: List<InvoiceVO?>?) {
                var listaFacturas = result
                getImporteMaximoModel(result as List<InvoiceVO>)
                listaFacturas = filterList(listaFacturas as List<InvoiceVO>)
                invoices!!.postValue(listaFacturas!!)

                // Se oculta el progress bar
                _isLoading.postValue(false)
            }
        })

        // Se ejecuta el caso de uso
        Executors.newCachedThreadPool().execute(casoUso)
    }

    private val invoiceRepository: InvoiceRepositoryInterface
        private get() = if (hasInternetConnection()) {
            InvoiceApiRepository(getApplication<Application>().applicationContext)
        } else InvoiceDbRepository(getApplication<Application>().applicationContext)

    private fun hasInternetConnection(): Boolean {
        val connected: Boolean
        val cm =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nInfo = cm.activeNetworkInfo
        connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
        return connected
    }

    private fun filterList(listaFacturas: List<InvoiceVO>): List<InvoiceVO> {
        val filteredList: MutableList<InvoiceVO> = ArrayList(listaFacturas)
        val checks = ArrayList<String>()
        if (myWrapper.isPagadas) {
            checks.add(PAGADA_STRING)
        }
        if (myWrapper.isPendientesPago) {
            checks.add(PENDIENTE_PAGO_STRING)
        }
        if (myWrapper.isAnuladas) {
            checks.add(ANULADA_STRING)
        }
        if (myWrapper.isCuotaFija) {
            checks.add(CUOTA_FIJA_STRING)
        }
        if (myWrapper.isPlanPago) {
            checks.add(PLAN_PAGO_STRING)
        }
        if (!myWrapper.hasta.isEmpty() && !myWrapper.desde.isEmpty()) {
            val iter = filteredList.iterator()
            while (iter.hasNext()) {
                val invoice = iter.next()
                val fechaImporte =
                    DateTime.parse(invoice.fecha, DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT))
                val hasta = DateTime.parse(
                    myWrapper.hasta,
                    DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT)
                )
                val desde = DateTime.parse(
                    myWrapper.desde,
                    DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT)
                )
                if (fechaImporte.isAfter(hasta) || fechaImporte.isBefore(desde)) {
                    iter.remove()
                }
            }
        } else if (!myWrapper.hasta.isEmpty()) {
            val iter = filteredList.iterator()
            while (iter.hasNext()) {
                val invoice = iter.next()
                val fechaImporte =
                    DateTime.parse(invoice.fecha, DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT))
                val hasta = DateTime.parse(
                    myWrapper.hasta,
                    DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT)
                )
                if (fechaImporte.isAfter(hasta)) {
                    iter.remove()
                }
            }
        } else if (!myWrapper.desde.isEmpty()) {
            val iter = filteredList.iterator()
            while (iter.hasNext()) {
                val invoice = iter.next()
                val fechaImporte =
                    DateTime.parse(invoice.fecha, DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT))
                val desde = DateTime.parse(
                    myWrapper.desde,
                    DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT)
                )
                if (fechaImporte.isBefore(desde)) {
                    iter.remove()
                }
            }
        }
        if (myWrapper.max > 0) {
            val iter = filteredList.iterator()
            while (iter.hasNext()) {
                val invoice = iter.next()
                if (invoice.importeOrdenacion > myWrapper.max) {
                    iter.remove()
                }
            }
        }
        if (checks.size > 0) {
            val iter = filteredList.iterator()
            while (iter.hasNext()) {
                val invoice = iter.next()
                if (!checks.contains(invoice.descEstado)) {
                    iter.remove()
                }
            }
        }
        return filteredList
    }

    fun setWrapper(myWrapper: FiltersWrapper) {
        this.myWrapper = myWrapper
    }

    private fun getImporteMaximoModel(listaFacturas: List<InvoiceVO>) {
        var max = 0
        for (i in listaFacturas) {
            if (i.importeOrdenacion >= max) {
                max = i.importeOrdenacion.toInt()
            }
        }
        importeMaximo = max
    }

    private fun setInvoices(invoices: MutableLiveData<List<InvoiceVO>>) {
        this.invoices = invoices
    }

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        setInvoices(MutableLiveData())
        _isLoading = MutableLiveData()
        myWrapper = FiltersWrapper()
        importeMaximo = 0
    }
}