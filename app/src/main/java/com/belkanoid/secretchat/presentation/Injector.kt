package com.belkanoid.secretchat.presentation

import com.belkanoid.secretchat.di.MessengerComponent

class Injector private constructor(){
    companion object {
        fun get(): MessengerComponent = MessengerApplication.get().component
    }
}