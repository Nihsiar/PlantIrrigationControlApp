package com.example.nihsiar.yggdrasilkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_monitor.*
import java.util.*

class activityMonitor : AppCompatActivity() {

    //region variables
    private val REQUEST_ENABLE_BT = 1
    private val btManager = BluetoothManager()
    private val handler = DatabaseHandler(this,null,null,1)

    //endregion

    private val thread = object : Thread() {
        override fun run() {
            try {
                while (!this.isInterrupted) {
                    Thread.sleep(1000)
                    runOnUiThread {
                        tvLevel.text = btManager.run()

                        if ((tvLevel.text as String).toInt() > 76)
                            tvStatus.text = "Flooded"
                        else if ((tvLevel.text as String).toInt() <= 75)
                            tvStatus.text = "Wet"
                        else if ((tvLevel.text as String).toInt() <= 50)
                            tvStatus.text = "Dry"
                        else
                            tvStatus.text = "Very Dry"
                    }
                    if(this.isInterrupted){
                        Toast.makeText(applicationContext,"Interrupt",Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: InterruptedException) {
                return
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)

        //Action bar
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        //intent for bluetooth
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

        button.setOnClickListener {
            handler.insertValues(
                tvLocation.text.toString(),
                tvLevel.text.toString(),
                tvStatus.text.toString()
            )
            Toast.makeText(applicationContext,"Successfully added to the Database",Toast.LENGTH_SHORT).show()
        }

        button2.setOnClickListener {
            btManager.write("1")
            var stats:String = tvStatus.text.toString()
            handler.insertValues(tvLocation.text.toString(),tvLevel.text.toString(),stats)
            Toast.makeText(applicationContext,"Added to the Database",Toast.LENGTH_SHORT).show()
        }
    }

    //region for title bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                thread.interrupt()
                btManager.bluetoothDeactivate()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }
    //endregion

    //region status of the app
    override fun onPause() {
        super.onPause()
        btManager.bluetoothDeactivate()
    }

    override fun onStop() {
        super.onStop()
        btManager.bluetoothDeactivate()
    }

    override fun onDestroy() {
        super.onDestroy()
        btManager.bluetoothDeactivate()
    }
    //endregion

    //for bluetooth intent
    @SuppressLint()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == Activity.RESULT_OK) {
                var isSuccess = btManager.initializeSocket()

                if(isSuccess == true) {
                    tvLocation.text = "Vassallios"
                    tvLevel.text = btManager.run()

                    if ((tvLevel.text as String).toInt() < 76)
                        tvStatus.text = "Flooded"
                    if ((tvLevel.text as String).toInt() >= 75)
                        tvStatus.text = "Wet"
                    if ((tvLevel.text as String).toInt() >= 50)
                        tvStatus.text = "Dry"
                    if ((tvLevel.text as String).toInt() >= 20)
                        tvStatus.text = "Very Dry"

                    Toast.makeText(applicationContext, "Connected to the Device", Toast.LENGTH_SHORT).show()

                    thread.start()
                    }
                    else{
                        btManager.bluetoothDeactivate()
                        Toast.makeText(applicationContext, "The Device is Off", Toast.LENGTH_SHORT).show()
                        val myIntent = Intent(this@activityMonitor, MainActivity::class.java)
                        this@activityMonitor.startActivity(myIntent)
                    }
            }
            if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(applicationContext,"Rejected the Bluetooth",Toast.LENGTH_SHORT).show()
                val myIntent = Intent(this@activityMonitor, MainActivity::class.java)
                this@activityMonitor.startActivity(myIntent)
            }
        }
    }
}
