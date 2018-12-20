package br.com.sudosu.terminal.domain

object Command {

    private val ESC: Byte = 0x1B
    private val FS: Byte = 0x1C
    private val GS: Byte = 0x1D
    private val US: Byte = 0x1F
    private val DLE: Byte = 0x10
    private val DC4: Byte = 0x14
    private val DC1: Byte = 0x11
    private val SP: Byte = 0x20
    private val NL: Byte = 0x0A
    private val FF: Byte = 0x0C
    val PIECE = 0xFF.toByte()
    val NUL = 0x00.toByte()


    //    Inicialização da impressora
    var ESC_Init = byteArrayOf(ESC, '@'.toByte())

    /**
     * Ordem de impressão
     */
    //Imprimir e embrulhar
    var LF = byteArrayOf(NL)

    // print and wrap
    var ESC_J = byteArrayOf(ESC, 'J'.toByte(), 0x00)
    var ESC_d = byteArrayOf(ESC, 'd'.toByte(), 0x00)

    //  Imprimir uma página de autoteste
    var US_vt_eot = byteArrayOf(US, DC1, 0x04)

    //  Comando Beep
    var ESC_B_m_n = byteArrayOf(ESC, 'B'.toByte(), 0x00, 0x00)

    //    Instruções de corte
    var GS_V_n = byteArrayOf(GS, 'V'.toByte(), 0x00)
    var GS_V_m_n = byteArrayOf(GS, 'V'.toByte(), 'B'.toByte(), 0x00)
    var GS_i = byteArrayOf(ESC, 'i'.toByte())
    var GS_m = byteArrayOf(ESC, 'm'.toByte())

    /**
     * Comando de configuração de caracteres
     */
// Define o espaçamento certo do caracteres
    var ESC_SP = byteArrayOf(ESC, SP, 0x00)

    //    Definir formato de fonte de impressão de caracteres
    var ESC_ExclamationMark = byteArrayOf(ESC, '!'.toByte(), 0x00)

    //    Definir a altura e largura da fonte
    var GS_ExclamationMark = byteArrayOf(GS, '!'.toByte(), 0x00)

    //    Definir impressão reversa
    var GS_B = byteArrayOf(GS, 'B'.toByte(), 0x00)

    //Cancelar / selecionar impressão de rotação de 90 graus
    var ESC_V = byteArrayOf(ESC, 'V'.toByte(), 0x00)

    //    Selecione fonte font (principalmente código ASCII)
    var ESC_M = byteArrayOf(ESC, 'M'.toByte(), 0x00)

    //Characteres Internacionais
    var ESC_R = byteArrayOf(ESC, 'R'.toByte(), 0x00)

    //Selecione / cancele as instruções em negrito
    var ESC_G = byteArrayOf(ESC, 'G'.toByte(), 0x00)
    var ESC_E = byteArrayOf(ESC, 'E'.toByte(), 0x00)

    //Selecione / cancele o modo de impressão invertido
    var ESC_LeftBrace = byteArrayOf(ESC, '{'.toByte(), 0x00)

    //Definir a altura do sublinhado (caractere)
    var ESC_Minus = byteArrayOf(ESC, 45, 0x00)

    //Modo de caractere
    var FS_dot = byteArrayOf(FS, 46)

    //Modo de caracteres chineses
    var FS_and = byteArrayOf(FS, '&'.toByte())

    //Definir o modo de impressão de caracteres chineses
    var FS_ExclamationMark = byteArrayOf(FS, '!'.toByte(), 0x00)

    //Definir a altura do sublinhado (caracteres chineses)
    var FS_Minus = byteArrayOf(FS, 45, 0x00)

    //Defina o espaçamento esquerdo e direito de caracteres chineses
    var FS_S = byteArrayOf(FS, 'S'.toByte(), 0x00, 0x00)

    //Selecione a página de código de caractere
    var ESC_t = byteArrayOf(ESC, 't'.toByte(), 0x00)

    /**
     * Instrução de formatação
     */
    //Definir o espaçamento de linha padrão
    var ESC_Two = byteArrayOf(ESC, 50)

    //Definir o espaçamento entre linhas
    var ESC_Three = byteArrayOf(ESC, 51, 0x00)

    //Definir o modo de alinhamento
    var ESC_Align = byteArrayOf(ESC, 'a'.toByte(), 0x00)

    //Definir a margem esquerda
    var GS_LeftSp = byteArrayOf(GS, 'L'.toByte(), 0x00, 0x00)

    //Definir a posição de impressão absoluta
    //Defina a posição atual para o início da linha (nL + nH x 256).
    //Este comando é ignorado se a posição definida estiver fora da área de impressão especificada
    var ESC_Relative = byteArrayOf(ESC, '$'.toByte(), 0x00, 0x00)

    //Definir posição de impressão relativa
    var ESC_Absolute = byteArrayOf(ESC, 92, 0x00, 0x00)

    //Definir a largura da área de impressão
    var GS_W = byteArrayOf(GS, 'W'.toByte(), 0x00, 0x00)

    /**
     * Instrução de status
     */
    //Instrução de transferência de status em tempo real
    var DLE_eot = byteArrayOf(DLE, 0x04, 0x00)

    //Instrução em caixa de dinheiro em tempo real
    var DLE_DC4 = byteArrayOf(DLE, DC4, 0x00, 0x00, 0x00)

    //Instrução padrão da caixa de dinheiro
    var ESC_p = byteArrayOf(ESC, 'F'.toByte(), 0x00, 0x00, 0x00)

    /**
     * Instrução de configuração de código de barras
     */
    //Selecione o método de impressão HRI
    var GS_H = byteArrayOf(GS, 'H'.toByte(), 0x00)

    //Definir a altura do código de barras
    var GS_h = byteArrayOf(GS, 'h'.toByte(), 0xa2.toByte())

    //Definir a largura do código de barras
    var GS_w = byteArrayOf(GS, 'w'.toByte(), 0x00)

    //Definir fonte de caracteres HRI
    var GS_f = byteArrayOf(GS, 'f'.toByte(), 0x00)

    //Instruções de deslocamento de código de barras à esquerda
    var GS_x = byteArrayOf(GS, 'x'.toByte(), 0x00)

    //Comando de código de barras de impressão
    var GS_k = byteArrayOf(GS, 'k'.toByte(), 'A'.toByte(), FF)

    //Instruções relacionadas com o código QR
    var GS_k_m_v_r_nL_nH = byteArrayOf(ESC, 'Z'.toByte(), 0x03, 0x03, 0x08, 0x00, 0x00)
}