package com.iamsdt.pssd.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iamsdt.pssd.di.ViewModelKey
import com.iamsdt.pssd.ext.ViewModelFactory
import com.iamsdt.pssd.ui.main.MainVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule{

    @Binds
    @IntoMap
    @ViewModelKey(MainVM::class)
    internal abstract fun mainVM(mainVM: MainVM):ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory)
            : ViewModelProvider.Factory
}