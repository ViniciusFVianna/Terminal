package br.com.sudosu.terminal.extensions

//Utilizar onClick ao invés de setOnClickListner
fun android.view.View.onClick(l: (v: android.view.View?) -> Unit){
    setOnClickListener(l)
}