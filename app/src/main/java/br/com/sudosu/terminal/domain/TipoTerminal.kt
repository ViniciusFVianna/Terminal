package br.com.sudosu.terminal.domain

import br.com.sudosu.terminal.R

enum class TipoTerminal(val string: Int) {
    produtos(R.string.produtos),
    device(R.string.devices),
    favoritos(R.string.favoritos)
}