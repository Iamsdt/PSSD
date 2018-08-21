package com.iamsdt.pssd.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iamsdt.pssd.di.ViewModelKey
import com.iamsdt.pssd.ext.ViewModelFactory
import com.iamsdt.pssd.ui.details.DetailsVM
import com.iamsdt.pssd.ui.favourite.FavouriteVM
import com.iamsdt.pssd.ui.flash.FlashVM
import com.iamsdt.pssd.ui.main.MainVM
import com.iamsdt.pssd.ui.search.SearchVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainVM::class)
    internal abstract fun mainVM(mainVM: MainVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsVM::class)
    internal abstract fun detailsVM(mainVM: DetailsVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavouriteVM::class)
    internal abstract fun favVM(mainVM: FavouriteVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FlashVM::class)
    internal abstract fun falshVM(mainVM: FlashVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchVM::class)
    internal abstract fun searchVM(mainVM: SearchVM): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory)
            : ViewModelProvider.Factory
}