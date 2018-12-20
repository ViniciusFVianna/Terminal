package br.com.sudosu.terminal

import android.app.Application
import android.util.Log

class TerminalApplication: Application() {
    private val TAG = "TerminalApplication"

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
    companion object {
        private var appInstance: TerminalApplication? =null

        fun getInstance(): TerminalApplication{
            if (appInstance == null){
                throw IllegalStateException("Configure a classe de Application no AndroidManifest.xml")
            }
            return appInstance!!
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d(TAG, "TerminalApplication.onTerminate()")
    }
}