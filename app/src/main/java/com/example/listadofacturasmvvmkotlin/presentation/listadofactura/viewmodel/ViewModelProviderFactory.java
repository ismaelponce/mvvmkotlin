package com.example.listadofacturasmvvmkotlin.presentation.listadofactura.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelProviderFactory implements ViewModelProvider.Factory {

    private final Application app;
    public ViewModelProviderFactory(Application application) {
        this.app = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)  new ListadoViewModel(app);
    }
}
