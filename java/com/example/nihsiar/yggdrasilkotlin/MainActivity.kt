package com.example.nihsiar.yggdrasilkotlin

import android.app.Instrumentation
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnMonitor.setOnClickListener {
            val myIntent = Intent(this@MainActivity, activityMonitor::class.java)
            this@MainActivity.startActivity(myIntent)
        }

        btnAbout.setOnClickListener {
            val myIntent = Intent(this@MainActivity, activityAbout::class.java)
            this@MainActivity.startActivity(myIntent)
        }

        btnRecord.setOnClickListener {
            val myIntent = Intent(this@MainActivity, activityRecord::class.java)
            this@MainActivity.startActivity(myIntent)
        }

    }
}
