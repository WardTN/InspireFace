package com.example.inspireface_example

import android.app.Application

class App : Application() {

    companion object {
        @Volatile
        private var mInstance: App? = null

        fun getInstance(): App {
            return mInstance ?: synchronized(this) {
                mInstance ?: App().also { mInstance = it }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

    override fun onTerminate() {
        super.onTerminate()
        mInstance = null
    }
}
