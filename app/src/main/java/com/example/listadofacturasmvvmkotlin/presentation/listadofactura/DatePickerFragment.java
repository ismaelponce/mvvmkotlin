package com.example.listadofacturasmvvmkotlin.presentation.listadofactura;

import static com.example.listadofacturasmvvmkotlin.utils.Constants.LOCAL_DATE_TIME_FORMAT;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;


    private String desdeMaxDate = "";
    private String hastaMinDate = "";

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(getActivity(), listener, year, month, day);

        if (!desdeMaxDate.equals("")) {
            DateTime dateParsed = DateTime.parse(desdeMaxDate, DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT));
            picker.getDatePicker().setMaxDate(dateParsed.getMillis());
        } else {
            picker.getDatePicker().setMaxDate(c.getTimeInMillis());
        }

        if (!hastaMinDate.equals("")) {
            DateTime dateParsed = DateTime.parse(hastaMinDate, DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT));
            picker.getDatePicker().setMinDate(dateParsed.getMillis());
        }

        return picker;
    }


    public void setDesdeMaxDate(String desdeMaxDate) {
        this.desdeMaxDate = desdeMaxDate;
    }

    public void setHastaMinDate(String hastaMinDate) {
        this.hastaMinDate = hastaMinDate;
    }


}
