package br.com.sudosu.terminal.domain

import android.graphics.Bitmap
import zj.com.customize.sdk.Other
import kotlin.experimental.or


object PrintPicture {

    /**
     *Imprimir a função bitmap
     *Esta função imprime uma linha como uma imagem, portanto, não é fácil processar erros.
     * @param mBitmap
     * @param nWidth
     * @param nMode
     * @return
     */
    fun POS_PrintBMP(mBitmap: Bitmap, nWidth: Int, nMode: Int): ByteArray {
        // Primeiro, preto e branco, em seguida, chame a função para dimensionar o bitmap.
        val width = (nWidth + 7) / 8 * 8
        var height = mBitmap.height * width / mBitmap.width
        height = (height + 7) / 8 * 8

        var rszBitmap = mBitmap
        if (mBitmap.width != width) {
            rszBitmap = Other.resizeImage(mBitmap, width, height)
        }

        val grayBitmap = Other.toGrayscale(rszBitmap)

        val dithered = Other.thresholdToBWPic(grayBitmap)

        return Other.eachLinePixToCmd(dithered, width, nMode)
    }

    /**
     * Imprimir imagens usando o bitmap inferior
     * Receber e imprimir primeiro
     * @param bmp
     * @return
     */
    fun Print_1D2A(bmp: Bitmap): ByteArray {

        /*
			* Imprimir imagens usando o bitmap inferior
            * Receber e imprimir primeiro
			 */
        val width = bmp.width
        val height = bmp.height
        val data = ByteArray(1024 * 10)
        data[0] = 0x1D
        data[1] = 0x2A
        data[2] = ((width - 1) / 8 + 1).toByte()
        data[3] = ((height - 1) / 8 + 1).toByte()
        var k: Byte = 0
        var position = 4
        var i: Int
        var j: Int
        var temp: Byte = 0
        i = 0
        while (i < width) {

            println("Entrar...I")
            j = 0
            while (j < height) {
                println("Entrar...J")
                if (bmp.getPixel(i, j) != -1) {
                    temp = temp or (0x80 shr k.toInt()).toByte()
                } // end if
                k++
                if (k.toInt() == 8) {
                    data[position++] = temp
                    temp = 0
                    k = 0
                } // end if k
                j++
            }// end for j
            if (k % 8 != 0) {
                data[position++] = temp
                temp = 0
                k = 0
            }
            i++

        }
        println("data$data")

        if (width % 8 != 0) {
            i = height / 8
            if (height % 8 != 0) i++
            j = 8 - width % 8
            k = 0
            while (k < i * j) {
                data[position++] = 0
                k++
            }
        }
        return data
    }
}