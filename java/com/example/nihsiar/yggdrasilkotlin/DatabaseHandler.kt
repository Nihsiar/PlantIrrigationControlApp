package com.example.nihsiar.yggdrasilkotlin

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@Suppress("UNREACHABLE_CODE", "UNUSED_CHANGED_VALUE")
@SuppressLint("ByteOrderMark")
class DatabaseHandler (context: Context, name:String?,
                       factory: SQLiteDatabase.CursorFactory?,
                       version : Int ) : SQLiteOpenHelper(context,
    DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "plantationDB.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_Plants = "Plantation"
        const val colPotTranID = "Pot_ID"
        const val colMoislevel = "Moisture"
        const val colTime = "TIME"
        const val colStatus = "Status"
        const val colLocations = "Location"
        const val colDate = "DATE"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createPlantationTable = (
                "CREATE TABLE $TABLE_Plants(" +
                        "$colPotTranID INTEGER PRIMARY KEY, " +
                        "$colLocations TEXT," +
                        "$colMoislevel TEXT," +
                        "$colStatus TEXT," +
                        "$colTime TEXT," +
                        "$colDate TEXT);")
        db.execSQL(createPlantationTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXIST $TABLE_Plants")
        onCreate(db)
    }

    @SuppressLint("SimpleDateFormat")

    fun insertValues (moisturelvl: String?, locations: String?, stats:String?){
        //region time and date
        val calendar = Calendar.getInstance()
        val currentDateTime = LocalDateTime.now()
        val df = SimpleDateFormat("MM / dd ")
        val timeadded = currentDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        val dateadded = df.format(calendar.time)
        //endregion

        val db= this.writableDatabase
        db.execSQL("INSERT INTO $TABLE_Plants ("+"'$colLocations',"+"'$colMoislevel',"+"'$colDate',"+"'$colTime',"+" '$colStatus') VALUES(" +
                "'$locations', " + "'$moisturelvl'," + "'$dateadded'," + "'$timeadded', " + " '$stats');")
        db.close()
    }

    fun getLength():Int{
        val db = this.writableDatabase
        val cursor = db.rawQuery("Select* from $TABLE_Plants", null)
        return cursor.count
    }

    fun selectedView(col:Int,row:Int):String?{
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_Plants",null)

        //region initialize arrays
        var id = arrayOfNulls<String>(getLength())
        var loc = arrayOfNulls<String>(getLength())
        var stat = arrayOfNulls<String>(getLength())
        var date = arrayOfNulls<String>(getLength())
        var tim = arrayOfNulls<String>(getLength())
        var lvl = arrayOfNulls<String>(getLength())
        //endregion

        var i=0
        while(cursor.moveToNext()){
            id.set(i, cursor.getString(0))
            loc.set(i, cursor.getString(1))
            stat.set(i, cursor.getString(2))
            date.set(i,cursor.getString(3))
            tim.set(i, cursor.getString(4))
            lvl.set(i, cursor.getString(5))
            i += 1
        }

        cursor.close()
        db.close()

        //region return values
        if(col == 0)
            return id[row]
        else if ( col == 1)
            return stat[row]
        else if (col == 2)
            return loc[row]
        else if (col == 3)
            return lvl[row]
        else if (col == 4)
            return tim[row]
        else ( return date[row])
        //endregion
    }

}

