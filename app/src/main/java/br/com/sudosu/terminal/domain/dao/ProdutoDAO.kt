package br.com.sudosu.terminal.domain.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import br.com.sudosu.terminal.domain.Produto

@Dao
interface ProdutoDAO {
    @Query("SELECT * FROM produto where id = :arg=0")
    fun getById(id : Long): Produto?
    @Query("SELECT * FROM produto")
    fun findAll(): List<Produto>
    @Insert
    fun insert(produto: Produto)
    @Delete
    fun delete(produto: Produto)
}