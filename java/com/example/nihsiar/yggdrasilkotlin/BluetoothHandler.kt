package com.example.nihsiar.yggdrasilkotlin

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.InvocationTargetException
import java.util.*
//import jdk.nashorn.internal.objects.ArrayBufferView.buffer
import android.system.Os.socket
import java.lang.reflect.Method

class BluetoothManager{

    //region variables
    var mBluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var mBluetoothDevice : BluetoothDevice
    private lateinit var mBlueToothSocket : BluetoothSocket
    private lateinit var output: OutputStream
    private lateinit var inputStream: InputStream
    lateinit var mBluetoothDeviceName:String
    private val ARDUINO_MAC_ADDRESS = "00:21:13:00:03:92"
    private val My_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    //endregion

    companion object {
        val EXTRA_ADDRESS = "device address"
        val REQUEST_ENABLE_BLUETOOTH = 1
        val TAG = "bluetooth String"
    }

    fun bluetoothDeactivate(): Boolean? {
        if (mBluetoothAdapter!!.enable()) {
            mBluetoothAdapter!!.disable()
            return true
        } else
            return false
    }

    fun initializeSocket():Boolean {
        var isSuccess=false
        try {
            mBluetoothDevice = mBluetoothAdapter!!.getRemoteDevice(ARDUINO_MAC_ADDRESS)
            isSuccess = bluetoothConnect(mBluetoothDevice)
            if (isSuccess)
                mBluetoothDeviceName=mBluetoothDevice.name
            else
                return false
        } catch (e: Exception) {
            Log.i("Bluetooth Error", "Cannot Connect")
            return false
        }
        return true
    }

    private fun bluetoothConnect(device: BluetoothDevice):Boolean {
        try {
            mBlueToothSocket = device.createRfcommSocketToServiceRecord(My_UUID)
        } catch (e: Exception) {
            Log.i("Error", "Failed to Create the Socket")
            return false
        }

        mBluetoothAdapter?.cancelDiscovery()

        try {
            mBlueToothSocket.connect()
        } catch (e: Exception) {
            try {
                mBlueToothSocket.close()
                return false
            } catch (s: Exception) {
                return false
            }
        }

        if (mBlueToothSocket.isConnected()) {
            try {
                inputStream = mBlueToothSocket.inputStream
                output = mBlueToothSocket.outputStream
            } catch (e: Exception) {
                Log.i("Error", "Failed to get I/O streams")
                return false
            }
        }
        return true
    }

    fun run():String {
        val buffer = ByteArray(256)
        var bytesCount: Int
        lateinit var readMessage:String
        var sc:Scanner

        while (true) {
            try {
                bytesCount = inputStream.available()
                if(bytesCount!=0) {
                    bytesCount = inputStream.read(buffer)
                    readMessage = String(buffer, 0, bytesCount)
                    val str = readMessage
                    sc = Scanner(str).useDelimiter("\n");
                    return sc.nextLine()
                }
            } catch (e: IOException) {
                Log.i("Error", "mmBuffer")
            }
        }
        return readMessage
    }

    fun write(data: String) {
        val mmBuffer :ByteArray = data.toByteArray()
        try {
            output.write(mmBuffer)
        } catch (e: IOException) {
            Log.i("Error", "Failed to send data")
        }

    }
}
