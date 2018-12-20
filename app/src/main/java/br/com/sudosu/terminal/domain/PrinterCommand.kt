package br.com.sudosu.terminal.domain

import zj.com.customize.sdk.Other
import java.io.UnsupportedEncodingException

object PrinterCommand {
    /**
     * Inicialização da impressora
     * @return
     */
    fun POS_Set_PrtInit(): ByteArray {

        return Other.byteArraysToBytes(arrayOf(Command.ESC_Init))
    }

    /**
     * Imprimir e embrulhar
     * @return
     */
    fun POS_Set_LF(): ByteArray {

        return Other.byteArraysToBytes(arrayOf(Command.LF))
    }

    /**
     * Imprimir e alimentar (0 ~ 255)
     * @param feed
     * @return
     */
    fun POS_Set_PrtAndFeedPaper(feed: Int): ByteArray? {
        if ((feed > 255) or (feed < 0))
            return null

        Command.ESC_J[2] = feed.toByte()

        return Other.byteArraysToBytes(arrayOf(Command.ESC_J))
    }

    /**
     * Imprimir uma página de autoteste
     * @return
     */
    fun POS_Set_PrtSelfTest(): ByteArray {

        return Other.byteArraysToBytes(arrayOf(Command.US_vt_eot))
    }

    /**
     * Comando Beep
     * @param m  Quantidade de bip
     * @param t  Tempo de cada bipe
     * @return
     */
    fun POS_Set_Beep(m: Int, t: Int): ByteArray? {

        if ((m < 1 || m > 9) or (t < 1 || t > 9))
            return null

        Command.ESC_B_m_n[2] = m.toByte()
        Command.ESC_B_m_n[3] = t.toByte()

        return Other.byteArraysToBytes(arrayOf(Command.ESC_B_m_n))
    }

    /**
     * Comando do cortador (papel de alimentação para a posição do cortador e papel cortado)
     * @param cut  0~255
     * @return
     */
    fun POS_Set_Cut(cut: Int): ByteArray? {
        if ((cut > 255) or (cut < 0))
            return null

        Command.GS_V_m_n[3] = cut.toByte()
        return Other.byteArraysToBytes(arrayOf(Command.GS_V_m_n))
    }

    /**
     * Instrução caixa de dinheiro
     * @param nMode
     * @param nTime1
     * @param nTime2
     * @return
     */
    fun POS_Set_Cashbox(nMode: Int, nTime1: Int, nTime2: Int): ByteArray? {

        if ((nMode < 0 || nMode > 1) or (nTime1 < 0) or (nTime1 > 255) or (nTime2 < 0) or (nTime2 > 255))
            return null
        Command.ESC_p[2] = nMode.toByte()
        Command.ESC_p[3] = nTime1.toByte()
        Command.ESC_p[4] = nTime2.toByte()

        return Other.byteArraysToBytes(arrayOf(Command.ESC_p))
    }

    /**
     * Definir a posição de impressão absoluta
     * @param absolute
     * @return
     */
    fun POS_Set_Absolute(absolute: Int): ByteArray? {
        if ((absolute > 65535) or (absolute < 0))
            return null

        Command.ESC_Relative[2] = (absolute % 0x100).toByte()
        Command.ESC_Relative[3] = (absolute / 0x100).toByte()

        return Other.byteArraysToBytes(arrayOf(Command.ESC_Relative))
    }

    /**
     * Definir posição de impressão relativa
     * @param relative
     * @return
     */
    fun POS_Set_Relative(relative: Int): ByteArray? {
        if ((relative < 0) or (relative > 65535))
            return null

        Command.ESC_Absolute[2] = (relative % 0x100).toByte()
        Command.ESC_Absolute[3] = (relative / 0x100).toByte()

        return Other.byteArraysToBytes(arrayOf(Command.ESC_Absolute))
    }

    /**
     * Definir a margem esquerda
     * @param left
     * @return
     */
    fun POS_Set_LeftSP(left: Int): ByteArray? {
        if ((left > 255) or (left < 0))
            return null

        Command.GS_LeftSp[2] = (left % 100).toByte()
        Command.GS_LeftSp[3] = (left / 100).toByte()

        return Other.byteArraysToBytes(arrayOf(Command.GS_LeftSp))
    }

    /**
     * Definir o modo de alinhamento
     * @param align
     * @return
     */
    fun POS_S_Align(align: Int): ByteArray? {
        if ((align < 0 || align > 2) or (align < 48 || align > 50))
            return null

        val data = Command.ESC_Align
        data[2] = align.toByte()
        return data
    }

    /**
     * Definir a largura da área de impressão
     * @param width
     * @return
     */
    fun POS_Set_PrintWidth(width: Int): ByteArray? {
        if ((width < 0) or (width > 255))
            return null

        Command.GS_W[2] = (width % 100).toByte()
        Command.GS_W[3] = (width / 100).toByte()

        return Other.byteArraysToBytes(arrayOf(Command.GS_W))
    }

    /**
     * Definir o espaçamento de linha padrão
     * @return
     */
    fun POS_Set_DefLineSpace(): ByteArray {

        return Command.ESC_Two
    }

    /**
     * Definir o espaçamento entre linhas
     * @param space
     * @return
     */
    fun POS_Set_LineSpace(space: Int): ByteArray? {
        if ((space < 0) or (space > 255))
            return null

        Command.ESC_Three[2] = space.toByte()

        return Other.byteArraysToBytes(arrayOf(Command.ESC_Three))
    }

    /**
     * Selecione a página de código de caractere
     * @param page
     * @return
     */
    fun POS_Set_CodePage(page: Int): ByteArray? {
        if (page > 255)
            return null

        Command.ESC_t[2] = page.toByte()

        return Other.byteArraysToBytes(arrayOf(Command.ESC_t))
    }

    /**
     * Imprimir documento de texto
     * @param pszString     A corda a ser impressa
     * @param encoding      Imprimir código correspondente do caractere
     * @param codepage      Definir a página de código (0--255)
     * @param nWidthTimes   Largura dupla (0-4)
     * @param nHeightTimes  Altura dupla (0-4)
     * @param nFontType     Tipo de fonte (válido apenas para o código Ascii) (0,1 48,49)
     */
    fun POS_Print_Text(pszString: String?, encoding: String, codepage: Int,
                       nWidthTimes: Int, nHeightTimes: Int, nFontType: Int): ByteArray? {

        if (codepage < 0 || codepage > 255 || pszString == null || "" == pszString || pszString.length < 1) {
            return null
        }

        val pbString: ByteArray?
        try {
            pbString = pszString.toByteArray(charset(encoding))
        } catch (e: UnsupportedEncodingException) {
            return null
        }

        val intToWidth = byteArrayOf(0x00, 0x10, 0x20, 0x30)
        val intToHeight = byteArrayOf(0x00, 0x01, 0x02, 0x03)
        Command.GS_ExclamationMark[2] = (intToWidth[nWidthTimes] + intToHeight[nHeightTimes]).toByte()

        Command.ESC_t[2] = codepage.toByte()

        Command.ESC_M[2] = nFontType.toByte()

        return if (codepage == 0) {

            Other.byteArraysToBytes(arrayOf<ByteArray?>(Command.GS_ExclamationMark, Command.ESC_t, Command.FS_and, Command.ESC_M, pbString))
        } else {

            Other.byteArraysToBytes(arrayOf(Command.GS_ExclamationMark, Command.ESC_t, Command.FS_dot, Command.ESC_M, pbString))
        }
    }

    /**
     * Instrução negrito (o bit mais baixo é 1 válido)
     * @param bold
     * @return
     */
    fun POS_Set_Bold(bold: Int): ByteArray {

        Command.ESC_E[2] = bold.toByte()
        Command.ESC_G[2] = bold.toByte()

        return Other.byteArraysToBytes(arrayOf(Command.ESC_E, Command.ESC_G))
    }

    /**
     * Defina o modo de impressão invertido (válido quando o bit mais baixo é 1)
     * @param brace
     * @return
     */
    fun POS_Set_LeftBrace(brace: Int): ByteArray {

        Command.ESC_LeftBrace[2] = brace.toByte()
        return Other.byteArraysToBytes(arrayOf(Command.ESC_LeftBrace))
    }

    /**
     * Definir sublinhado
     * @param line
     * @return
     */
    fun POS_Set_UnderLine(line: Int): ByteArray? {

        if (line < 0 || line > 2)
            return null

        Command.ESC_Minus[2] = line.toByte()
        Command.FS_Minus[2] = line.toByte()

        return Other.byteArraysToBytes(arrayOf(Command.ESC_Minus, Command.FS_Minus))
    }

    /**
     * Selecione o tamanho da fonte (altura dupla)
     * @param size
     * @return
     */
    fun POS_Set_FontSize(size1: Int, size2: Int): ByteArray? {
        if ((size1 < 0) or (size1 > 7) or (size2 < 0) or (size2 > 7))
            return null

        val intToWidth = byteArrayOf(0x00, 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70)
        val intToHeight = byteArrayOf(0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07)
        Command.GS_ExclamationMark[2] = (intToWidth[size1] + intToHeight[size2]).toByte()
        return Other.byteArraysToBytes(arrayOf(Command.GS_ExclamationMark))
    }

    /**
     * Definir impressão reversa
     * @param inverse
     * @return
     */
    fun POS_Set_Inverse(inverse: Int): ByteArray {

        Command.GS_B[2] = inverse.toByte()

        return Other.byteArraysToBytes(arrayOf(Command.GS_B))
    }

    /**
     * Definir a impressão de 90 graus de rotação
     * @param rotate
     * @return
     */
    fun POS_Set_Rotate(rotate: Int): ByteArray? {
        if (rotate < 0 || rotate > 1)
            return null
        Command.ESC_V[2] = rotate.toByte()
        return Other.byteArraysToBytes(arrayOf(Command.ESC_V))
    }

    /**
     * Defini os charactesres internacionais
     * @param character
     * @return
     */
    fun POS_Set_Character(character: Int):ByteArray?{
        if (character < 0 || character > 15)
            return null
        Command.ESC_R[2] = character.toByte()
        return Other.byteArraysToBytes(arrayOf(Command.ESC_R))
    }

    /**
     * Selecione fonte font
     * @param font
     * @return
     */
    fun POS_Set_ChoseFont(font: Int): ByteArray? {
        if ((font > 1) or (font < 0))
            return null

        Command.ESC_M[2] = font.toByte()
        return Other.byteArraysToBytes(arrayOf(Command.ESC_M))

    }
    //****************************************A seguinte função é uma função pública******************************************************//
    /**
     *Função de impressão de código QR
     * @param str                     Imprimir dados do código QR
     * @param nVersion                Tipo de código QR
     * @param nErrorCorrectionLevel   Nível de correção de erros
     * @param nMagnification          Ampliação
     * @return
     */
    fun getBarCommand(str: String, nVersion: Int, nErrorCorrectionLevel: Int,
                      nMagnification: Int): ByteArray? {

        if ((nVersion < 0) or (nVersion > 19) or (nErrorCorrectionLevel < 0) or (nErrorCorrectionLevel > 3)
            or (nMagnification < 1) or (nMagnification > 8)) {
            return null
        }

        var bCodeData: ByteArray?
        try {
            bCodeData = str.toByteArray(charset("GBK"))

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return null
        }

        val command = ByteArray(bCodeData.size + 7)

        command[0] = 27
        command[1] = 90
        command[2] = nVersion.toByte()
        command[3] = nErrorCorrectionLevel.toByte()
        command[4] = nMagnification.toByte()
        command[5] = (bCodeData.size and 0xff).toByte()
        command[6] = (bCodeData.size and 0xff00 shr 8).toByte()
        System.arraycopy(bCodeData, 0, command, 7, bCodeData.size)

        return command
    }

    /**
     * Imprimir código de barras unidimensional
     * @param str                   Imprimir caracteres de código de barras
     * @param nType                 Tipo de código de barras (65 ~ 73)
     * @param nWidthX               Largura do código de barras
     * @param nHeight               Altura do código de barras
     * @param nHriFontType          Fonte HRI
     * @param nHriFontPosition      Posição HRI
     * @return
     */
    fun getCodeBarCommand(str: String, nType: Int, nWidthX: Int, nHeight: Int,
                          nHriFontType: Int, nHriFontPosition: Int): ByteArray? {

        if ((nType < 0x41) or (nType > 0x49) or (nWidthX < 2) or (nWidthX > 6)
            or (nHeight < 1) or (nHeight > 255) or (str.length == 0))
            return null

        var bCodeData: ByteArray?
        try {
            bCodeData = str.toByteArray(charset("GBK"))

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return null
        }

        val command = ByteArray(bCodeData.size + 16)

        command[0] = 29
        command[1] = 119
        command[2] = nWidthX.toByte()
        command[3] = 29
        command[4] = 104
        command[5] = nHeight.toByte()
        command[6] = 29
        command[7] = 102
        command[8] = (nHriFontType and 0x01).toByte()
        command[9] = 29
        command[10] = 72
        command[11] = (nHriFontPosition and 0x03).toByte()
        command[12] = 29
        command[13] = 107
        command[14] = nType.toByte()
        command[15] = bCodeData.size.toByte()
        System.arraycopy(bCodeData, 0, command, 16, bCodeData.size)


        return command
    }

    /**
     * Defina o modo de impressão (selecione a fonte (fonte: A fonte: B), negrito, fonte de largura alta dupla (máximo de 4 vezes a altura e largura))
     * @param str          Cadeia impressa
     * @param bold         Negrito
     * @param font         Selecione a fonte
     * @param widthsize    Largura dupla
     * @param heigthsize   Altura dupla
     * @return
     */
    fun POS_Set_Font(str: String, bold: Int, font: Int, widthsize: Int, heigthsize: Int): ByteArray? {

        if ((str.length == 0) or (widthsize < 0) or (widthsize > 4) or (heigthsize < 0) or (heigthsize > 4)
            or (font < 0) or (font > 1))
            return null

        var strData: ByteArray? = null
        try {
            strData = str.toByteArray(charset("GBK"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return null
        }

        val command = ByteArray(strData.size + 9)

        val intToWidth = byteArrayOf(0x00, 0x10, 0x20, 0x30)//Até quatro vezes mais largo
        val intToHeight = byteArrayOf(0x00, 0x01, 0x02, 0x03)//Até quatro vezes maior

        command[0] = 27
        command[1] = 69
        command[2] = bold.toByte()
        command[3] = 27
        command[4] = 77
        command[5] = font.toByte()
        command[6] = 29
        command[7] = 33
        command[8] = (intToWidth[widthsize] + intToHeight[heigthsize]).toByte()

        System.arraycopy(strData, 0, command, 9, strData.size)
        return command
    }
//**********************************************************************************************************//
}