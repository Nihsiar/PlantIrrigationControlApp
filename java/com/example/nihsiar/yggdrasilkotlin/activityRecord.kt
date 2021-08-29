package com.example.nihsiar.yggdrasilkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_record.*

class activityRecord : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val db = DatabaseHandler(this,null,null,1)
        val row = db.getLength()
        createTable(row, 6)
    }

    fun createTable(rows: Int, cols: Int) {
        val db = DatabaseHandler(this, null, null, 1)

        for (i in 0 until rows) {
            val row = TableRow(tableLayout2.context)
            row.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            for (j in 0 until cols) {
                val textview = TextView(this)
                textview.apply {
                    layoutParams = TableRow.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT, 0.5F
                    )
                    textview.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    text = db.selectedView(j, i)

                }
                row.addView(textview)
            }
            tableLayout2.addView(row)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

}
