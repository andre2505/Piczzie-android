package com.ziggy.kdo.base.application

import android.app.Application
import android.content.Context
import com.ziggy.kdo.base.factory.ActivityBindingModule
import com.ziggy.kdo.base.factory.FragmentBindingModule
import com.ziggy.kdo.base.factory.ViewModelModule
import com.ziggy.kdo.base.factory.ViewModelFactoryModule
import com.ziggy.kdo.network.configuration.NetworkModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton
import dagger.BindsInstance

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ViewModelFactoryModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        ActivityBindingModule::class,
        FragmentBindingModule::class
    ]
)

interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: BaseApplication)
}