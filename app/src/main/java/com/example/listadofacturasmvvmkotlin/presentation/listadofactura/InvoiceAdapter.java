package com.example.listadofacturasmvvmkotlin.presentation.listadofactura;


import static com.example.listadofacturasmvvmkotlin.utils.Constants.INVOICE_PRICE_FORMAT_EURO;
import static com.example.listadofacturasmvvmkotlin.utils.Constants.LOCAL_DATE_TIME_FORMAT;
import static com.example.listadofacturasmvvmkotlin.utils.Constants.LOCAL_DATE_TIME_OUTPUT_FORMAT;
import static com.example.listadofacturasmvvmkotlin.utils.Constants.PAGADA_STRING;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO;
import com.example.listadofacturasmvvmkotlin.databinding.ItemInvoiceBinding;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Locale;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(InvoiceVO position);
    }

    private final ArrayList<InvoiceVO> invoices;
    private final OnItemClickListener listener;

    public InvoiceAdapter(ArrayList<InvoiceVO> invoices, OnItemClickListener listener) {
        this.invoices = invoices;
        this.listener = listener;

    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InvoiceViewHolder(ItemInvoiceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        holder.bind(invoices.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }


    public static class InvoiceViewHolder extends RecyclerView.ViewHolder {

        ItemInvoiceBinding binding;

        public InvoiceViewHolder(ItemInvoiceBinding b) {
            super(b.getRoot());
            binding = b;
        }

        public void bind(InvoiceVO invoice, OnItemClickListener listener){

            // Se hace bind del estado
            binding.tvEstado.setText(invoice.getDescEstado());

            if (PAGADA_STRING.equals(invoice.getDescEstado())) {
                binding.tvEstado.setVisibility(View.GONE);
            } else {
                binding.tvEstado.setVisibility(View.VISIBLE);
                binding.tvEstado.setTextColor(Color.RED);
            }

            // Se formatea y hace bind de la fecha
            DateTime dateTime = DateTime.parse(invoice.getFecha(), DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT));
            DateTimeFormatter fmd = DateTimeFormat.forPattern(LOCAL_DATE_TIME_OUTPUT_FORMAT);
            binding.tvFecha.setText(fmd.print(dateTime));

            // Se hace bind del importe
            binding.tvImporte.setText(String.format(Locale.CANADA, INVOICE_PRICE_FORMAT_EURO, invoice.getImporteOrdenacion()));

            itemView.setOnClickListener(v -> listener.onItemClick(invoice));
        }

    }
}
