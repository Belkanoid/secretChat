package com.belkanoid.secretchat.di

import android.content.Context
import com.belkanoid.secretchat.presentation.MainActivity
import com.belkanoid.secretchat.presentation.Interactor.Interactor
import dagger.*
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, ViewModelModule::class])
interface MessengerComponent {

    fun inject(mainActivity: MainActivity)

    fun getInteractor(): Interactor

    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance context: Context
        ): MessengerComponent
    }
}