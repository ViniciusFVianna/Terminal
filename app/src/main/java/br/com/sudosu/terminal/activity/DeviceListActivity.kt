package br.com.sudosu.terminal.activity

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import br.com.sudosu.terminal.R
import kotlinx.android.synthetic.main.activity_device_list.*
import org.jetbrains.anko.toast

class DeviceListActivity :BaseActivity() {

    private var mBluetoothAdapter: BluetoothAdapter? = null
    private lateinit var mPairedDevices: Set<BluetoothDevice>
    val REQUSET_ENABLE_BLUETOOTH = 1

    companion object {
        val EXTRA_ADDRESS: String = "Device_adress"
        val EXTRA_NOME: String = "Device_nome"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_list)

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null){
            toast(R.string.no_bluetooth)
            return
        }
        if (!mBluetoothAdapter!!.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUSET_ENABLE_BLUETOOTH)
        }

        select_device_refresh.setOnClickListener{ pairedDeviceList() }

    }

    private fun pairedDeviceList() {
        mPairedDevices = mBluetoothAdapter!!.bondedDevices
        val list : ArrayList<BluetoothDevice> = ArrayList()

        if (mPairedDevices.isNotEmpty()) {
            for (device: BluetoothDevice in mPairedDevices) {
                list.add(device)
                Log.i("device", ""+device.name)
            }
        } else {
            toast(R.string.no_paired)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        select_device_list.adapter = adapter
        select_device_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = list[position]
            val address: String = device.address
            val nome: String = device.name

            val intent = Intent(this, ProdutoActivity::class.java)
            intent.putExtra(EXTRA_NOME, nome)
            intent.putExtra(EXTRA_ADDRESS, address)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUSET_ENABLE_BLUETOOTH){
            if (resultCode == Activity.RESULT_OK){
                if (mBluetoothAdapter!!.isEnabled){
                    toast(R.string.bluetooth_enabled)
                }else{
                    toast(R.string.bluetooth_disabled)
                }
            }else if (resultCode == Activity.RESULT_CANCELED){
                toast(R.string.bluetooth_canceled)
            }
        }
    }
}