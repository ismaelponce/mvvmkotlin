package com.example.listadofacturasmvvmkotlin.presentation.listadofactura

import androidx.appcompat.app.AppCompatActivity
import com.example.listadofacturasmvvmkotlin.presentation.listadofactura.viewmodel.ListadoViewModel
import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO
import com.example.listadofacturasmvvmkotlin.data.model.FiltersWrapper
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DividerItemDecoration
import android.annotation.SuppressLint
import kotlin.Throws
import androidx.lifecycle.ViewModelProvider
import com.example.listadofacturasmvvmkotlin.presentation.listadofactura.viewmodel.ViewModelProviderFactory
import android.content.Intent
import com.google.gson.Gson
import com.example.listadofacturasmvvmkotlin.R
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.listadofacturasmvvmkotlin.databinding.ActivityMainBinding
import com.example.listadofacturasmvvmkotlin.presentation.listadofactura.InvoiceAdapter.OnItemClickListener
import java.io.IOException
import java.util.ArrayList
import com.example.listadofacturasmvvmkotlin.utils.Constants.KEYMAX
import com.example.listadofacturasmvvmkotlin.utils.Constants.KEYWRAPPER


class ListadoViewActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var viewmodel: ListadoViewModel? = null
    private var adapter: InvoiceAdapter? = null
    private val list = ArrayList<InvoiceVO>()
    private var myWrapper = FiltersWrapper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inicializarPantalla()
        try {
            initViewModel()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        bindListeners()
    }

    private fun inicializarPantalla() {
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        initRecycler()
    }

    private fun initRecycler() {
        binding!!.rvFacturas.layoutManager = LinearLayoutManager(this)
        adapter = InvoiceAdapter(list, object : OnItemClickListener {
            override fun onItemClick(position: InvoiceVO?) {
                showAlertDialog()
            }
        })
        binding!!.rvFacturas.adapter = adapter
        binding!!.rvFacturas.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    @Throws(IOException::class)
    private fun initViewModel() {
        viewmodel = ViewModelProvider(this, ViewModelProviderFactory(application)).get(
            ListadoViewModel::class.java
        )

        // Se carga el listado de facturas
        viewmodel!!.loadInvoices()

        // Se actualiza el recycler mediante el adapter cuando cambia el listado en el viewmodel
        viewmodel!!.invoices.observe(this, { invoices: List<InvoiceVO>? ->
            list.clear()
            list.addAll(invoices!!)
            adapter!!.notifyDataSetChanged()
        })

        // Se muestra el progress bar mientras se trae el listado, cuando llega, se oculta
        viewmodel!!.isLoading.observe(this, { aBoolean: Boolean ->
            if (aBoolean) {
                binding!!.progress.visibility = View.VISIBLE
            } else {
                binding!!.progress.visibility = View.GONE
            }
        })
    }

    private fun bindListeners() {
        binding!!.toolbar.filterButton.setOnClickListener { v: View? ->
            val intent = Intent(this, FilterViewActivity::class.java)
            if (viewmodel!!.importeMaximo > 0) {
                intent.putExtra(KEYMAX, viewmodel!!.importeMaximo)
            }
            viewmodel!!.setWrapper(myWrapper)
            val myWrapperParsed = Gson().toJson(myWrapper)
            intent.putExtra(KEYWRAPPER, myWrapperParsed)
            startActivityForResult(intent, 99)
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.alertDialogTitle)
        builder.setMessage(R.string.alertDialogMessage)
        builder.setPositiveButton(R.string.alertDialogPositiveBtn) { dialog: DialogInterface?, id: Int -> }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            assert(data != null)
            myWrapper =
                Gson().fromJson(data!!.getStringExtra(KEYWRAPPER), FiltersWrapper::class.java)

            // Se pasan los parametros de filtrado al viewmodel, traidos desde la vista de filtrado
            viewmodel!!.setWrapper(myWrapper)

            // Se vuelve a cargar el listado, para que se apliquen los filtros
            viewmodel!!.loadInvoices()
        }
    }
}