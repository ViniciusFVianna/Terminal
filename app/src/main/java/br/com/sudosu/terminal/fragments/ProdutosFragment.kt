package br.com.sudosu.terminal.fragments

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.com.sudosu.terminal.R
import br.com.sudosu.terminal.activity.ProdutoActivity
import br.com.sudosu.terminal.adapter.ProdutoAdapter
import br.com.sudosu.terminal.domain.Produto
import br.com.sudosu.terminal.domain.ProdutoService
import br.com.sudosu.terminal.domain.TipoTerminal
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


open class ProdutosFragment : BaseFragment() {
    private var tipo = TipoTerminal.produtos
    protected var produtos = listOf<Produto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null){
            tipo = arguments!!.getSerializable("tipo") as TipoTerminal
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Retorn a view  /res/layout/fragments_produtos.xml
        val view = inflater?.inflate(R.layout.fragment_produtos, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Views
//        recyclerView.layoutManager = LinearLayoutManager(activity)
//        recyclerView.itemAnimator = DefaultItemAnimator()
//        recyclerView.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        taskProdutos()
    }
    open fun taskProdutos(){
        //Abre uma thread
        doAsync {
            //Busca os produtos
//            produtos = ProdutoService.getProdutos(tipo)
            //Atualiza a lista na UI Thread
            uiThread {
//                recyclerView.adapter = ProdutoAdapter(produtos){onClickProduto(it)}
            }
        }
    }
    open fun onClickProduto(produto: Produto){
//        activity.startActivity<ProdutoActivity>("produto" to produto)
    }
}
