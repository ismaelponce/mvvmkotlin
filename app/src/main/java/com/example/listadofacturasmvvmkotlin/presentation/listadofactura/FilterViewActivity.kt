package com.example.listadofacturasmvvmkotlin.presentation.listadofactura

import androidx.appcompat.app.AppCompatActivity
import com.example.listadofacturasmvvmkotlin.data.model.FiltersWrapper
import android.os.Bundle
import android.widget.EditText
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.SeekBar
import android.content.Intent
import com.google.gson.Gson
import android.app.Activity
import com.example.listadofacturasmvvmkotlin.presentation.listadofactura.DatePickerFragment
import android.app.DatePickerDialog.OnDateSetListener
import android.view.View
import android.widget.DatePicker
import com.example.listadofacturasmvvmkotlin.databinding.ActivityFilterViewBinding
import com.example.listadofacturasmvvmkotlin.utils.Constants.DESDE_DATE_PICKER_TAG
import com.example.listadofacturasmvvmkotlin.utils.Constants.HASTA_DATE_PICKER_TAG
import com.example.listadofacturasmvvmkotlin.utils.Constants.KEYMAX
import com.example.listadofacturasmvvmkotlin.utils.Constants.KEYWRAPPER

class FilterViewActivity : AppCompatActivity() {
    private var myWrapper = FiltersWrapper()
    private var binding: ActivityFilterViewBinding? = null
    private var desdeMaxDate = ""
    private var hastaMinDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterViewBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)

        // Se crea la seekbar
        createSeekbar()

        // Se observan los valores que puedan venir de otra actividad
        checkIntents()

        // Se añaden los listeners a los botones y los datepicker
        bindListeners()
    }

    private fun bindListeners() {
        binding!!.filterToolbar.closeButton.setOnClickListener { v: View? -> finish() }
        binding!!.etDesde.setOnClickListener { v: View ->
            showDatePickerDialog(
                DESDE_DATE_PICKER_TAG,
                v as EditText
            )
        }
        binding!!.etHasta.setOnClickListener { v: View ->
            showDatePickerDialog(
                HASTA_DATE_PICKER_TAG,
                v as EditText
            )
        }
        binding!!.quitarButton.setOnClickListener { v: View? -> quitFilters() }
        binding!!.aplicarButton.setOnClickListener { v: View? -> applyFilters() }
    }

    private fun createSeekbar() {
        binding!!.seekbarImporte.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val progressString = progress.toString()
                binding!!.tvImporteDinamico.text = progressString
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Se comenta metodo implementado por necesidad de interfaz pero innecesario
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Se comenta metodo implementado por necesidad de interfaz pero innecesario
            }
        })
    }

    fun checkIntents() {
        val intent = intent
        val importeMaximo = intent.getIntExtra(KEYMAX, 0)
        if (importeMaximo > 0) {
            binding!!.seekbarImporte.max = importeMaximo
        }
        myWrapper = Gson().fromJson(intent.getStringExtra(KEYWRAPPER), FiltersWrapper::class.java)
        binding!!.etDesde.setText(myWrapper.desde)
        binding!!.etHasta.setText(myWrapper.hasta)
        binding!!.seekbarImporte.progress = myWrapper.max
        binding!!.cbPagadas.isChecked = myWrapper.isPagadas
        binding!!.cbAnuladas.isChecked = myWrapper.isAnuladas
        binding!!.cbCuotaFija.isChecked = myWrapper.isCuotaFija
        binding!!.cbPendientesDePago.isChecked = myWrapper.isPendientesPago
        binding!!.cbPlanDePago.isChecked = myWrapper.isPlanPago
    }

    private fun quitFilters() {
        binding!!.etDesde.setText("")
        binding!!.etHasta.setText("")
        binding!!.seekbarImporte.progress = 0
        binding!!.cbPagadas.isChecked = false
        binding!!.cbAnuladas.isChecked = false
        binding!!.cbCuotaFija.isChecked = false
        binding!!.cbPendientesDePago.isChecked = false
        binding!!.cbPlanDePago.isChecked = false
    }

    private fun applyFilters() {
        val desde = binding!!.etDesde.text.toString()
        val hasta = binding!!.etHasta.text.toString()
        val stringMax = binding!!.tvImporteDinamico.text.toString()
        val max = stringMax.toInt()
        val pagadas = binding!!.cbPagadas.isChecked
        val anuladas = binding!!.cbAnuladas.isChecked
        val cuotaFija = binding!!.cbCuotaFija.isChecked
        val pendientesPago = binding!!.cbPendientesDePago.isChecked
        val planPago = binding!!.cbPlanDePago.isChecked
        myWrapper = FiltersWrapper(
            desde,
            hasta,
            max,
            pagadas,
            anuladas,
            cuotaFija,
            pendientesPago,
            planPago
        )
        val myWrapperParsed = Gson().toJson(myWrapper)
        val result = Intent()
        result.putExtra(KEYWRAPPER, myWrapperParsed)
        setResult(RESULT_OK, result)
        finish()
    }

    private fun showDatePickerDialog(tag: String, v: EditText) {
        val datePicker =
            DatePickerFragment.newInstance { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val months = month + 1
                val date = "$dayOfMonth/$months/$year"
                if (v.id == binding!!.etHasta.id) {
                    desdeMaxDate = date
                }
                if (v.id == binding!!.etDesde.id) {
                    hastaMinDate = date
                }
                v.setText(date)
            }
        if (v.id == binding!!.etDesde.id && !desdeMaxDate.isEmpty()) {
            datePicker.setDesdeMaxDate(desdeMaxDate)
        }
        if (v.id == binding!!.etHasta.id && !hastaMinDate.isEmpty()) {
            datePicker.setHastaMinDate(hastaMinDate)
        }
        datePicker.show(supportFragmentManager, tag)
    }
}