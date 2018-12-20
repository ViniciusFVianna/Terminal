package br.com.sudosu.terminal.domain

import br.com.sudosu.terminal.domain.dao.DatabaseManager

object FavoritoService{

        //busca todos os produtos
        fun getProdutos(): List<Produto>{
            val dao = DatabaseManager.getProdutoDAO()
            val produtos = dao.findAll()
            return produtos
        }
        //        Verifica se um produto está favoritado
        fun isFavorito(produto: Produto) : Boolean{
            val dao = DatabaseManager.getProdutoDAO()
            val exists = dao.getById(produto.id) != null
            return exists
        }
        //        Salva  o produto ou deleta
        fun favoritar(produto: Produto): Boolean{
            val dao = DatabaseManager.getProdutoDAO()
            val favorito = isFavorito(produto)
            if (favorito){
                //Remove dos favoritos
                dao.delete(produto)
                return false
            }
            //Adiciona nos favoritos
            dao.insert(produto)
            return true
        }
}