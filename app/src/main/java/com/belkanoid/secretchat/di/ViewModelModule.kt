package com.belkanoid.secretchat.di

import androidx.lifecycle.ViewModel
import com.belkanoid.secretchat.presentation.viewModel.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindsMainViewModel(viewModel: MainViewModel): ViewModel

}