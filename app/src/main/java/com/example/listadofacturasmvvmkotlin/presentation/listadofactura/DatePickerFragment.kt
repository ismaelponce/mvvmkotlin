package com.example.listadofacturasmvvmkotlin.presentation.listadofactura

import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.app.DatePickerDialog
import android.app.Dialog
import androidx.fragment.app.DialogFragment
import org.joda.time.format.DateTimeFormat
import com.example.listadofacturasmvvmkotlin.presentation.listadofactura.DatePickerFragment
import com.example.listadofacturasmvvmkotlin.utils.Constants.LOCAL_DATE_TIME_FORMAT
import org.joda.time.DateTime
import java.util.*

class DatePickerFragment : DialogFragment() {
    private var listener: OnDateSetListener? = null
    private var desdeMaxDate = ""
    private var hastaMinDate = ""
    fun setListener(listener: OnDateSetListener?) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]
        val picker = DatePickerDialog(requireActivity(), listener, year, month, day)
        if (desdeMaxDate != "") {
            val dateParsed =
                DateTime.parse(desdeMaxDate, DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT))
            picker.datePicker.maxDate = dateParsed.millis
        } else {
            picker.datePicker.maxDate = c.timeInMillis
        }
        if (hastaMinDate != "") {
            val dateParsed =
                DateTime.parse(hastaMinDate, DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT))
            picker.datePicker.minDate = dateParsed.millis
        }
        return picker
    }

    fun setDesdeMaxDate(desdeMaxDate: String) {
        this.desdeMaxDate = desdeMaxDate
    }

    fun setHastaMinDate(hastaMinDate: String) {
        this.hastaMinDate = hastaMinDate
    }

    companion object {
        fun newInstance(listener: OnDateSetListener?): DatePickerFragment {
            val fragment = DatePickerFragment()
            fragment.setListener(listener)
            return fragment
        }
    }
}