package br.com.sudosu.terminal.domain.dao

import android.arch.persistence.room.Room
import br.com.sudosu.terminal.TerminalApplication
import br.com.sudosu.terminal.domain.ProdutoDatabase

object DatabaseManager {
//    Singleton do Room: banco de dados
    private lateinit var dbIsntance: ProdutoDatabase

    object DatabaseManager{
        init {
            val appContext = TerminalApplication.getInstance().applicationContext
//            Configura o Room
            dbIsntance = Room.databaseBuilder(appContext, ProdutoDatabase::class.java, "produto.sqlite")
                .build()
        }
    }
    fun getProdutoDAO(): ProdutoDAO{
        return dbIsntance.produtoDao()
    }
}