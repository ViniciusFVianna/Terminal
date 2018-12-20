package br.com.sudosu.terminal.adapter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import br.com.sudosu.terminal.domain.TipoTerminal
import br.com.sudosu.terminal.fragments.FavoritoFragment
import br.com.sudosu.terminal.fragments.ProdutosFragment


class TabsAdapter(private  val context: Context, fm: FragmentManager): FragmentPagerAdapter(fm) {
    //Qtde de tabs
    override fun getCount() = 3
    //retorna o tipo pela posição
     fun getTipoProduto(position: Int) = when(position){
        0 -> TipoTerminal.produtos
        1 -> TipoTerminal.device
        else -> TipoTerminal.favoritos
    }

    //Título da Tab
    override fun getPageTitle(position: Int): CharSequence? {
        val tipo = getTipoProduto(position)
        return context.getString(tipo.string)
    }

    override fun getItem(position: Int): Fragment {
        if (position == 2){
            //favoritos
            return FavoritoFragment()
        }
        // produtos e devices
        val tipo = getTipoProduto(position)
        val f: Fragment = ProdutosFragment()
        f.arguments = Bundle()
        f.arguments!!.putSerializable("tipo", tipo)
        return f
    }
}