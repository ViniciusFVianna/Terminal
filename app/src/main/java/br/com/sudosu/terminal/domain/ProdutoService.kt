package br.com.sudosu.terminal.domain

import br.com.sudosu.terminal.domain.dao.DatabaseManager
import br.com.sudosu.terminal.util.HttpHelper
import okhttp3.Response

object ProdutoService {
//    private val BASSE_URL = "http://livrowebservice.com.br/rest/carros"
//
//    //Busca os produtos por tipo
//    fun getProdutos(tipo: TipoTerminal): List<Produto>{
//        val url = "$BASSE_URL/${tipo.name}"
//        val json = HttpHelper.get(url)
//        val produtos = fromJson<List<Produto>>(json)
//        return produtos
//    }
//
//    //Salva o produto
//    fun save(produto: Produto): Response{
//        //Faz POST do JSON produto
//        val json = HttpHelper.post(BASSE_URL, produto.toJson())
//        val response = formJson<Response>(json)
//        return response
//    }
//
//    //Deleta um produto
//    fun delete(produto: Produto): Response{
//        val url = "$BASSE_URL/${produto.id}"
//        val json = HttpHelper.delete(url)
//        val response = fromJson<Response>(json)
//        if (responseisOk){
//            //Se removeu do servidor, remove dos favoritos
//            val dao = DatabaseManager.getProdutoDAO()
//            dao.delete(produto)
//        }
//        return response
//    }

}