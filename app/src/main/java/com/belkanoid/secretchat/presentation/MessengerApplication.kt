package com.belkanoid.secretchat.presentation

import android.app.Application
import com.belkanoid.secretchat.di.DaggerMessengerComponent

class MessengerApplication: Application() {
    val component by lazy {
        DaggerMessengerComponent.factory().create(this)
    }
}