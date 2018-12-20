package br.com.sudosu.terminal.fragments

import br.com.sudosu.terminal.activity.ProdutoActivity
import br.com.sudosu.terminal.adapter.ProdutoAdapter
import br.com.sudosu.terminal.domain.FavoritoService
import br.com.sudosu.terminal.domain.Produto
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class FavoritoFragment : ProdutosFragment() {
    override fun taskProdutos(){
        doAsync {
            //Busca os produtos
            produtos = FavoritoService.getProdutos()
            uiThread {
//                recyclerView.adapter = ProdutoAdapter(produtos){ onClickProduto(it)}
            }
        }
    }
    override fun onClickProduto(produto: Produto){
        //Ao clicar no produto navegar para tela de detalhes
//        activity.startActivity<ProdutoActivity>("produto" to produto)
    }
}
