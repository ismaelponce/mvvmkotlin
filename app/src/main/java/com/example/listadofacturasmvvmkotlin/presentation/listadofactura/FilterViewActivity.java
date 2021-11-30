package com.example.listadofacturasmvvmkotlin.presentation.listadofactura;

import static com.example.listadofacturasmvvmkotlin.utils.Constants.*;
import static com.example.listadofacturasmvvmkotlin.utils.Constants.DESDE_DATE_PICKER_TAG;
import static com.example.listadofacturasmvvmkotlin.utils.Constants.HASTA_DATE_PICKER_TAG;
import static com.example.listadofacturasmvvmkotlin.utils.Constants.KEYMAX;
import static com.example.listadofacturasmvvmkotlin.utils.Constants.KEYWRAPPER;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.listadofacturasmvvmkotlin.data.model.FiltersWrapper;
import com.example.listadofacturasmvvmkotlin.databinding.ActivityFilterViewBinding;
import com.google.gson.Gson;

public class FilterViewActivity extends AppCompatActivity {

    private FiltersWrapper myWrapper = new FiltersWrapper();
    private ActivityFilterViewBinding binding;
    private String desdeMaxDate = "";
    private String hastaMinDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Se crea la seekbar
        createSeekbar();

        // Se observan los valores que puedan venir de otra actividad
        checkIntents();

        // Se añaden los listeners a los botones y los datepicker
        bindListeners();
    }

    private void bindListeners() {

        binding.filterToolbar.closeButton.setOnClickListener(v -> finish());

        binding.etDesde.setOnClickListener( v -> showDatePickerDialog(DESDE_DATE_PICKER_TAG, (EditText) v));

        binding.etHasta.setOnClickListener ( v -> showDatePickerDialog(HASTA_DATE_PICKER_TAG, (EditText) v ));

        binding.quitarButton.setOnClickListener (v -> quitFilters());
        binding.aplicarButton.setOnClickListener (v -> applyFilters());

    }

    private void createSeekbar() {
        binding.seekbarImporte.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String progressString = String.valueOf(progress);
                binding.tvImporteDinamico.setText(progressString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Se comenta metodo implementado por necesidad de interfaz pero innecesario
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Se comenta metodo implementado por necesidad de interfaz pero innecesario
            }
        });
    }

    public void checkIntents(){
        Intent intent = getIntent();
        int importeMaximo = intent.getIntExtra(KEYMAX, 0);

        if (importeMaximo > 0) {
            binding.seekbarImporte.setMax(importeMaximo);
        }

        myWrapper = new Gson().fromJson(intent.getStringExtra(KEYWRAPPER), FiltersWrapper.class);

        binding.etDesde.setText(myWrapper.getDesde());
        binding.etHasta.setText(myWrapper.getHasta());
        binding.seekbarImporte.setProgress(myWrapper.getMax());
        binding.cbPagadas.setChecked(myWrapper.isPagadas());
        binding.cbAnuladas.setChecked(myWrapper.isAnuladas());
        binding.cbCuotaFija.setChecked(myWrapper.isCuotaFija());
        binding.cbPendientesDePago.setChecked(myWrapper.isPendientesPago());
        binding.cbPlanDePago.setChecked(myWrapper.isPlanPago());

    }

    private void quitFilters() {
        binding.etDesde.setText("");
        binding.etHasta.setText("");
        binding.seekbarImporte.setProgress(0);
        binding.cbPagadas.setChecked(false);
        binding.cbAnuladas.setChecked(false);
        binding.cbCuotaFija.setChecked(false);
        binding.cbPendientesDePago.setChecked(false);
        binding.cbPlanDePago.setChecked(false);
    }

    private void applyFilters() {
        String desde = binding.etDesde.getText().toString();
        String hasta = binding.etHasta.getText().toString();
        String stringMax = binding.tvImporteDinamico.getText().toString();
        int max = Integer.parseInt(stringMax);
        boolean pagadas = binding.cbPagadas.isChecked();
        boolean anuladas = binding.cbAnuladas.isChecked();
        boolean cuotaFija = binding.cbCuotaFija.isChecked();
        boolean pendientesPago = binding.cbPendientesDePago.isChecked();
        boolean planPago = binding.cbPlanDePago.isChecked();

        myWrapper = new FiltersWrapper(
                desde,
                hasta,
                max,
                pagadas,
                anuladas,
                cuotaFija,
                pendientesPago,
                planPago
        );

        String myWrapperParsed = new Gson().toJson(myWrapper);

        Intent result = new Intent();
        result.putExtra(KEYWRAPPER, myWrapperParsed);

        setResult(RESULT_OK, result);
        finish();
    }

    private void showDatePickerDialog(String tag, EditText v){

        DatePickerFragment datePicker = DatePickerFragment.newInstance((view, year, month, dayOfMonth) -> {

            int months = month + 1;
            String date = dayOfMonth + "/" + months + "/" + year;

            if (v.getId() == binding.etHasta.getId()) {
                desdeMaxDate = date;
            }
            if (v.getId() == binding.etDesde.getId()) {
                hastaMinDate = date;
            }

            v.setText(date);

        });

        if (v.getId() == binding.etDesde.getId() && !desdeMaxDate.isEmpty()) {
            datePicker.setDesdeMaxDate(desdeMaxDate);
        }

        if (v.getId() == binding.etHasta.getId() && !hastaMinDate.isEmpty()) {
            datePicker.setHastaMinDate(hastaMinDate);
        }

        datePicker.show(getSupportFragmentManager(), tag);
    }
}