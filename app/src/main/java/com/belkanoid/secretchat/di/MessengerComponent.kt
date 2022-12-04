package com.belkanoid.secretchat.di

import android.content.Context
import com.belkanoid.secretchat.MainActivity
import dagger.*

@Component(modules = [DataModule::class, ViewModelModule::class])
interface MessengerComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance context: Context
        ): MessengerComponent
    }
}