package br.com.sudosu.terminal.domain

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity(tableName = "produto")
class Produto() : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var nome = ""
    var qtde: Int = 0
    var valor: Double = 0.0

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.id)
        dest.writeString(this.nome)
        dest.writeInt(this.qtde)
        dest.writeDouble(this.valor)
    }
    fun readFromParcel(parcel: Parcel){
        this.id = parcel.readLong()
        this.nome = parcel.readString()
        this.qtde = parcel.readInt()
        this.valor = parcel.readDouble()

    }

    override fun toString(): String {
        return "\nProduto : $nome " +
                "\nQuantidade : $qtde " +
                "\nValor R$: $valor" +
                "\nTOTAL R$: ${calcularTotal()} ".format()
    }
    fun calcularTotal(): Double {
        var total = valor*qtde
        return total
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Produto> = object : Parcelable.Creator<Produto>{
            override fun createFromParcel(p: Parcel): Produto {
                val prod = Produto()
                prod.readFromParcel(p)
                return prod
            }

            override fun newArray(size: Int): Array<Produto?> {
                return arrayOfNulls(size)
            }
        }
    }
}