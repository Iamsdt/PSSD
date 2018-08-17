/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:32 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.di

import android.app.Application
import com.iamsdt.pssd.di.module.ActivityModule
import com.iamsdt.pssd.di.module.AppModule
import com.iamsdt.pssd.di.module.DBModule
import com.iamsdt.pssd.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ViewModelModule::class,
    ActivityModule::class,
    AppModule::class,
    DBModule::class])
interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    override fun inject(instance: DaggerApplication)
}