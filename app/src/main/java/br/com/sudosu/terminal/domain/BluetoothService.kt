package br.com.sudosu.terminal.domain

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import br.com.sudosu.terminal.activity.ProdutoActivity

open class BluetoothService {
    companion object {
        //        Debugging
        private val TAG = "BluetoothService"
        private val DEBUG = true

        // Name for the SDP record when creating server socket
        private val NAME = "POSTerminal"
        //UUID must be this
        // Unique UUID for this application
        private val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        // Member fields
        private lateinit var mAdapter: BluetoothAdapter
        private lateinit var mHandler: Handler
        private lateinit var mAcceptThread: AcceptThread
        private lateinit var mConnectThread: ConnectThread
        private lateinit var mConnectedThread: ConnectedThread
        private var mState: Int = 0

        // Constants that indicate the current connection state
        val STATE_NONE = 0       // we're doing nothing
        val STATE_LISTEN = 1     // now listening for incoming connections
        val STATE_CONNECTING = 2 // now initiating an outgoing connection
        val STATE_CONNECTED = 3  // now connected to a remote device

        var ErrorMessage = "No_Error_Message"
    }
    /**
     * Constructor. Prepares a new POSTerminal session.
     * @param context  The UI Activity Context
     * @param handler  A Handler to send messages back to the UI Activity
     */
    fun BluetoothService(context: Context, handler: Handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter()
        mState = STATE_NONE
        mHandler = handler
    }

    /**
     * Set the current state of the connection
     * @param state  An integer defining the current connection state
     */
    @Synchronized
    private fun setState(state: Int) {
        if (DEBUG) Log.d(TAG, "setState() $mState -> $state")
        mState = state

        // Give the new state to the Handler so the UI Activity can update
        mHandler?.obtainMessage(ProdutoActivity.MESSAGE_STATE_CHANGE, state, -1)!!.sendToTarget()
    }

    /**
     * Return the current connection state.  */
    @Synchronized
    fun getState(): Int {
        return mState
    }

    /**
     * Start the service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()  */
    @Synchronized
    fun start() {
        if (DEBUG) Log.d(TAG, "start")

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel()
            mConnectThread
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel()
            mConnectedThread
        }

        // Start the thread to listen on a BluetoothServerSocket
        if (mAcceptThread == null) {
            mAcceptThread = AcceptThread()
            mAcceptThread.start()
        }
        setState(STATE_LISTEN)
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
    @Synchronized
    fun connect(device: BluetoothDevice) {
        if (DEBUG) Log.d(TAG, "connect to: $device")

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel()
                mConnectThread
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel()
            mConnectedThread
        }

        // Start the thread to connect with the given device
        mConnectThread = ConnectThread(device)
        mConnectThread.start()
        setState(STATE_CONNECTING)
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    @Synchronized
    fun connected(socket: BluetoothSocket, device: BluetoothDevice) {
        if (DEBUG) Log.d(TAG, "connected")

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel()
            mConnectThread
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel()
            mConnectedThread
        }

        // Cancel the accept thread because we only want to connect to one device
        if (mAcceptThread != null) {
            mAcceptThread.cancel()
            mAcceptThread
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = ConnectedThread(socket)
        mConnectedThread.start()

        // Send the name of the connected device back to the UI Activity
        val msg = mHandler.obtainMessage(ProdutoActivity.MESSAGE_DEVICE_NAME)
        val bundle = Bundle()
        bundle.putString(ProdutoActivity.DEVICE_NAME, device.name)
        msg.data = bundle
        mHandler.sendMessage(msg)

        setState(STATE_CONNECTED)
    }

    /**
     * Stop all threads
     */
    @Synchronized
    fun stop() {
        if (DEBUG) Log.d(TAG, "stop")
        setState(STATE_NONE)
        if (true) {
            mConnectThread.cancel()
            mConnectThread
        }
        if (true) {
            mConnectedThread.cancel()
            mConnectedThread
        }
        if (true) {
            mAcceptThread.cancel()
            mAcceptThread
        }
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * @see ConnectedThread.write
     */
    fun write(out: ByteArray) {
        // Create temporary object
        var r: ConnectedThread? = null
        // Synchronize a copy of the ConnectedThread
        synchronized(this) {
            if (mState != STATE_CONNECTED) return
            r = mConnectedThread
        }
        r?.write(out)
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private fun connectionFailed() {
        setState(STATE_LISTEN)

        // Send a failure message back to the Activity
        val msg = mHandler.obtainMessage(ProdutoActivity.MESSAGE_TOAST)
        val bundle = Bundle()
        bundle.putString(ProdutoActivity.TOAST, "Unable to connect device")
        msg.data = bundle
        mHandler.sendMessage(msg)
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private fun connectionLost() {
        //setState(STATE_LISTEN);

        // Send a failure message back to the Activity
        val msg = mHandler.obtainMessage(ProdutoActivity.MESSAGE_TOAST)
        val bundle = Bundle()
        bundle.putString(ProdutoActivity.TOAST, "Device connection was lost")
        msg.data = bundle
        mHandler.sendMessage(msg)
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private inner class AcceptThread : Thread() {
        // The local server socket
        private val mmServerSocket: BluetoothServerSocket?

        init {
            var tmp: BluetoothServerSocket? = null

            // Create a new listening server socket
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID)
            } catch (e: IOException) {
                Log.e(TAG, "listen() failed", e)
            }

            mmServerSocket = tmp
        }

        override fun run() {
            if (DEBUG) Log.d(TAG, "BEGIN mAcceptThread" + this)
            name = "AcceptThread"
            var socket: BluetoothSocket?

            // Listen to the server socket if we're not connected
            while (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket!!.accept()
                } catch (e: IOException) {
                    Log.e(TAG, "accept() failed", e)
                    break
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized(this@BluetoothService) {
                        when (mState) {
                            STATE_LISTEN, STATE_CONNECTING ->
                                // Situation normal. Start the connected thread.
                                connected(socket, socket.remoteDevice)
                            STATE_NONE, STATE_CONNECTED ->
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close()
                                } catch (e: IOException) {
                                    Log.e(TAG, "Could not close unwanted socket", e)
                                }

                            else -> {
                            }
                        }
                    }
                }
            }
            if (DEBUG) Log.i(TAG, "END mAcceptThread")
        }

        fun cancel() {
            if (DEBUG) Log.d(TAG, "cancel " + this)
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "close() of server failed", e)
            }

        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private inner class ConnectThread(private val mmDevice: BluetoothDevice) : Thread() {
        private val mmSocket: BluetoothSocket?

        init {
            var tmp: BluetoothSocket? = null

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID)
            } catch (e: IOException) {
                Log.e(TAG, "create() failed", e)
            }

            mmSocket = tmp
        }

        override fun run() {
            Log.i(TAG, "BEGIN mConnectThread")
            name = "ConnectThread"

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery()

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket!!.connect()
            } catch (e: IOException) {
                connectionFailed()
                // Close the socket
                try {
                    mmSocket!!.close()
                } catch (e2: IOException) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2)
                }

                // Start the service over to restart listening mode
                this@BluetoothService.start()
                return
            }

            // Reset the ConnectThread because we're done
            synchronized(this@BluetoothService) {
                mConnectThread
            }

            // Start the connected thread
            connected(mmSocket, mmDevice)
        }

        fun cancel() {
            try {
                mmSocket!!.close()
            } catch (e: IOException) {
                Log.e(TAG, "close() of connect socket failed", e)
            }

        }
    }
    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread(socket: BluetoothSocket) : Thread() {
        lateinit var mmSocket: BluetoothSocket
        lateinit var mmInStream: InputStream
        lateinit var mmOutStream: OutputStream

        fun ConnectedThread(socket: BluetoothSocket) {
            Log.d(TAG, "create ConnectedThread")
            mmSocket = socket
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.inputStream
                tmpOut = socket.outputStream
            } catch (e: IOException) {
                Log.e(TAG, "temp sockets not created", e)
            }

            mmInStream = tmpIn!!
            mmOutStream = tmpOut!!
        }

        override fun run() {
            Log.i(TAG, "BEGIN mConnectedThread")
            var bytes: Int

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    val buffer = ByteArray(256)
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer)
                    if (bytes > 0) {
                        // Send the obtained bytes to the UI Activity
                        mHandler.obtainMessage(ProdutoActivity.MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget()
                    } else {
                        Log.e(TAG, "disconnected")
//                        connectionLosst()

                        //add by chongqing jinou
                        if (mState != STATE_NONE) {
                            Log.e(TAG, "disconnected")
                            // Start the service over to restart listening mode
                            start()
                        }
                        break
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "disconnected", e)
//                    connectionLost()

                    //add by chongqing jinou
                    if (mState != STATE_NONE) {
                        // Start the service over to restart listening mode
                        start()
                    }
                    break
                }

            }
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        fun write(buffer: ByteArray) {
            try {
                mmOutStream.write(buffer)
                mmOutStream.flush()//Esvazie o cache
//                 if (buffer.length > 3000) //
//                {
//                  byte[] readata = new byte[1];
//                  SPPReadTimeout(readata, 1, 5000);
//                }
//                Log.i("BTPWRITE", String(buffer, "GBK"))
                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(ProdutoActivity.MESSAGE_WRITE, -1, -1, buffer)
                    .sendToTarget()
            } catch (e: IOException) {
                Log.e(TAG, "Exception during write", e)
            }

        }

        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                Log.e(TAG, "close() of connect socket failed", e)
            }

        }
    }//fecha ConnectedThread
}