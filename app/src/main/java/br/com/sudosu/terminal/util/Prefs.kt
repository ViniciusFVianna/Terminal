package br.com.sudosu.terminal.util

import android.content.SharedPreferences
import br.com.sudosu.terminal.TerminalApplication

object Prefs {
    val PREF_ID = "produtos"
    private fun prefs(): SharedPreferences{
        val context = TerminalApplication.getInstance().applicationContext
        return context.getSharedPreferences(PREF_ID, 0)
    }
    fun setInt(flag: String, valor: Int) = prefs().edit().putInt(flag, valor).apply()
    fun getInt(flag: String) = prefs().getInt(flag, 0)
    fun setString(flag: String, valor: String) = prefs().edit().putString(flag, valor).apply()
    fun getstring(flag: String) = prefs().getString(flag,"")
    var tabIdx: Int
    get() = getInt("tabIdx")
    set(value) = setInt("tabIdx", value)
}