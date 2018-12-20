package br.com.sudosu.terminal.domain

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import br.com.sudosu.terminal.domain.dao.ProdutoDAO

// Define as classes que precisam ser persistidas e a versão do banco
@Database(entities = arrayOf(Produto::class), version = 1)
abstract class ProdutoDatabase: RoomDatabase() {
    abstract fun produtoDao():ProdutoDAO
}