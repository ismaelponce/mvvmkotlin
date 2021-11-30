package com.example.listadofacturasmvvmkotlin.presentation.listadofactura

import android.graphics.Color
import com.example.listadofacturasmvvmkotlin.data.model.InvoiceVO
import androidx.recyclerview.widget.RecyclerView
import com.example.listadofacturasmvvmkotlin.presentation.listadofactura.InvoiceAdapter.InvoiceViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.listadofacturasmvvmkotlin.databinding.ItemInvoiceBinding
import com.example.listadofacturasmvvmkotlin.utils.Constants.INVOICE_PRICE_FORMAT_EURO
import com.example.listadofacturasmvvmkotlin.utils.Constants.LOCAL_DATE_TIME_FORMAT
import com.example.listadofacturasmvvmkotlin.utils.Constants.LOCAL_DATE_TIME_OUTPUT_FORMAT
import com.example.listadofacturasmvvmkotlin.utils.Constants.PAGADA_STRING
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

class InvoiceAdapter(
    private val invoices: ArrayList<InvoiceVO>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<InvoiceViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: InvoiceVO?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        return InvoiceViewHolder(
            ItemInvoiceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        holder.bind(invoices[position], listener)
    }

    override fun getItemCount(): Int {
        return invoices.size
    }

    class InvoiceViewHolder(var binding: ItemInvoiceBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(invoice: InvoiceVO, listener: OnItemClickListener) {

            // Se hace bind del estado
            binding.tvEstado.text = invoice.descEstado
            if (PAGADA_STRING == invoice.descEstado) {
                binding.tvEstado.visibility = View.GONE
            } else {
                binding.tvEstado.visibility = View.VISIBLE
                binding.tvEstado.setTextColor(Color.RED)
            }

            // Se formatea y hace bind de la fecha
            val dateTime =
                DateTime.parse(invoice.fecha, DateTimeFormat.forPattern(LOCAL_DATE_TIME_FORMAT))
            val fmd = DateTimeFormat.forPattern(LOCAL_DATE_TIME_OUTPUT_FORMAT)
            binding.tvFecha.text = fmd.print(dateTime)

            // Se hace bind del importe
            binding.tvImporte.text =
                String.format(Locale.CANADA, INVOICE_PRICE_FORMAT_EURO, invoice.importeOrdenacion)
            itemView.setOnClickListener { v: View? -> listener.onItemClick(invoice) }
        }
    }
}