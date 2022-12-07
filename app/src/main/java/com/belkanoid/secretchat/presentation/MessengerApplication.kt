package com.belkanoid.secretchat.presentation

import android.app.Application
import com.belkanoid.secretchat.di.DaggerMessengerComponent
import com.belkanoid.secretchat.di.MessengerComponent

class MessengerApplication: Application() {
    lateinit var component: MessengerComponent
        private set

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        component = DaggerMessengerComponent.factory().create(this)
    }

    companion object {
        private var INSTANCE: MessengerApplication? = null

        fun get() : MessengerApplication = INSTANCE!!
    }
}