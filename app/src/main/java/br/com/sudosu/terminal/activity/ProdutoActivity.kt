package br.com.sudosu.terminal.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import br.com.sudosu.terminal.R
import java.io.IOException
import java.util.*
import android.widget.Toast
import br.com.sudosu.terminal.domain.*
import br.com.sudosu.terminal.extensions.string
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import br.com.sudosu.terminal.domain.PrinterCommand
import br.com.sudosu.terminal.domain.PrintPicture
import android.graphics.Bitmap
import br.com.sudosu.terminal.extensions.isEmpty
import br.com.sudosu.terminal.extensions.toast
import java.text.SimpleDateFormat


class ProdutoActivity : BaseActivity() {
    private val TAG = "ProdutoActivity"

    var edProd: EditText? = null
    var edQtde: EditText? = null
    var edValor: EditText? = null
    var btnPrint: Button? = null
    val produto: Produto? by lazy { intent.getParcelableExtra<Produto>("produto") }
    var btnDisconnect: Button? = null

    companion object {

        // Message types sent from the BluetoothService Handler
        val MESSAGE_STATE_CHANGE = 1
        val MESSAGE_READ = 2
        val MESSAGE_WRITE = 3
        val MESSAGE_DEVICE_NAME = 4
        val MESSAGE_TOAST = 5
        val MESSAGE_CONNECTION_LOST = 6
        val MESSAGE_UNABLE_CONNECT = 7

        // Intent request codes
        private val REQUEST_CONNECT_DEVICE = 1
        private val REQUEST_ENABLE_BT = 2
        private val REQUEST_CHOSE_BMP = 3
        private val REQUEST_CAMER = 4

        // Key names received from the BluetoothService Handler
        val DEVICE_NAME = "device_name"
        val TOAST = "toast"

        //QRcode
        private val QR_WIDTH = 350
        private val QR_HEIGHT = 350

        private val GBK = "GBK"
        val titulo: String = "PDV -  TÉSTE\n"
        val aviso: String = "\nCUPOM NÃO FISCAL"

        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        lateinit var m_address: String
        lateinit var m_name: String

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produto)

        m_address = intent.getStringExtra(DeviceListActivity.EXTRA_ADDRESS)
        m_name = intent.getStringExtra(DeviceListActivity.EXTRA_NOME)


        ConnectToDevice(this).execute()

        edProd = findViewById(R.id.produto_produto)
        edQtde = findViewById(R.id.qtde_produto)
        edValor = findViewById(R.id.valor_produto)
        btnPrint = findViewById(R.id.btn_print_produto)

        btnPrint?.setOnClickListener{
            if (edProd!!.isEmpty() && edQtde!!.isEmpty() && edValor!!.isEmpty()){
                toast(R.string.campos_vazios)
            }else{
                val p = produto?: Produto()
                p.nome = edProd!!.string
                p.qtde = edQtde!!.string.toInt()
                p.valor = edValor!!.string.toDouble()


                Command.ESC_Align[2] = 0x01
                sendCommand(Command.ESC_Align)
                Command.GS_ExclamationMark[2] = 0x11
                sendCommand(Command.GS_ExclamationMark)
                sendCommand(PrinterCommand.POS_Print_Text(titulo, GBK, 0,1,1,0)!!)
                sendCommand(PrinterCommand.POS_Set_DefLineSpace())
                Command.ESC_Align[2] = 0x00
                sendCommand(Command.ESC_Align)
                Command.GS_ExclamationMark[2] = 0x11
                sendCommand(Command.GS_ExclamationMark)
                sendCommand(PrinterCommand.POS_Set_PrtAndFeedPaper(10)!!)
                sendCommand(PrinterCommand.POS_Print_Text(p.toString(), GBK, 0,0,0,5)!!)
                Command.ESC_Align[2] = 0x00
                sendCommand(Command.ESC_Align)
                Command.GS_ExclamationMark[2] = 0x11
                sendCommand(Command.GS_ExclamationMark)
                sendCommand(PrinterCommand.POS_Set_PrtAndFeedPaper(5)!!)
                sendCommand(PrinterCommand.POS_Print_Text(aviso, GBK,0,1,1,1)!!)
                createImage()
                Command.ESC_Align[2] = 0x01
                sendCommand(Command.ESC_Align)
                Command.GS_ExclamationMark[2] = 0x11
                sendCommand(Command.GS_ExclamationMark)
                sendCommand(PrinterCommand.POS_Set_PrtAndFeedPaper(48)!!)
                sendCommand(Command.GS_V_m_n)
                sendCommand(PrinterCommand.POS_Set_PrtInit())
            }
        }
        btnDisconnect?.setOnClickListener {
            disconnect()
        }
    }

    fun sendCommand(input: ByteArray){
        if (m_bluetoothSocket != null){
            try {
                m_bluetoothSocket!!.outputStream.write(input)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    private fun sendCommand(input: String) {
        if (m_bluetoothSocket != null) {
            try{
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun disconnect() {
        if (m_bluetoothSocket != null) {
            try {
                m_bluetoothSocket!!.close()
                m_bluetoothSocket = null
                m_isConnected = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
    }

    fun createImage(){
        try {

            val writer = QRCodeWriter()

            val text = edProd!!.string
            Log.i(TAG, "Produto do qrcode: "+text)
            if (text == null || "".equals(text) || text.length < 1) {
                Toast.makeText(this, getText(R.string.vazio), Toast.LENGTH_SHORT).show()
                return
            }
            val matrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT)
            System.out.println("w:"+ matrix.width + "h: "+ matrix.height)

            val hints = Hashtable<EncodeHintType, String>()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            val bitMatrix = QRCodeWriter().encode(text,
                BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints)
            val pixels = IntArray(QR_WIDTH * QR_HEIGHT)
            for (y in 0 until QR_HEIGHT) {
                for (x in 0 until QR_WIDTH) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = -0x1000000
                    } else {
                        pixels[y * QR_WIDTH + x] = -0x1
                    }

                }
            }
            val bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                Bitmap.Config.ARGB_8888)

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT)

            val data = PrintPicture.POS_PrintBMP(bitmap, 384, 0)
            sendCommand(data)
            sendCommand(PrinterCommand.POS_Set_PrtAndFeedPaper(30)!!)
            sendCommand(PrinterCommand.POS_Set_Cut(1)!!)
            sendCommand(PrinterCommand.POS_Set_PrtInit())

        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            m_progress = ProgressDialog.show(context, "Connecting...", "please wait")

        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (m_bluetoothSocket == null || !m_isConnected) {
                    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = m_bluetoothAdapter.getRemoteDevice(m_address)
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    m_bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
            } else {
                m_isConnected = true
            }
            m_progress.dismiss()
        }
    }
}