package com.iamsdt.pssd.di.module

import androidx.lifecycle.ViewModelProvider
import com.iamsdt.pssd.ext.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule{

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory)
            : ViewModelProvider.Factory
}