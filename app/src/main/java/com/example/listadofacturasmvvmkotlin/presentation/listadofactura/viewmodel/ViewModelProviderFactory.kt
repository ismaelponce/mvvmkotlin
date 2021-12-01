package com.example.listadofacturasmvvmkotlin.presentation.listadofactura.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import com.example.listadofacturasmvvmkotlin.presentation.listadofactura.viewmodel.ListadoViewModel

class ViewModelProviderFactory(private val app: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListadoViewModel(app) as T
    }
}