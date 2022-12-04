package com.belkanoid.secretchat.di

import android.content.Context
import com.belkanoid.secretchat.data.repository.MessengerRepositoryImpl
import com.belkanoid.secretchat.data.util.SharedPreferences
import com.belkanoid.secretchat.domain.repository.MessengerRepository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @Binds
    fun bindsRepository(repositoryImpl: MessengerRepositoryImpl) : MessengerRepository

    companion object {
        fun bindsSharedPreferences(context: Context) : SharedPreferences {
            return SharedPreferences(context = context)
        }
    }

}