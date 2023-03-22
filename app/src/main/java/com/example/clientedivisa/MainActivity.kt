package com.example.clientedivisa

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader

class MainActivity : AppCompatActivity() {

    private val Callbacks = object : LoaderManager.LoaderCallbacks<Cursor> {

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            return CursorLoader(
                applicationContext,
                Uri.parse("content://com.example.proyectodivisa/monedas"),
                arrayOf("_ID","base_code","update","code","value"),
                null, null, null)
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {

            data?.apply {
                val adapter = SimpleCursorAdapter(applicationContext,
                    android.R.layout.simple_list_item_2,
                    this,
                    arrayOf("_ID","base_code","update","code","value"),
                    IntArray(4).apply {
                        set(3, android.R.id.text1)
                    } ,
                    SimpleCursorAdapter.IGNORE_ITEM_VIEW_TYPE
                )

                var items = adapter.count
                var datos = ""
                items --
                var uno = false


                for(i in 0..items) {
                    var cursor = adapter.getItem(i) as Cursor

                    if (cursor.getString(cursor.getColumnIndexOrThrow("code")) == "USD") {
                        if(uno) datos += "\n\n"
                        uno = true
                        val cursor = adapter.getItem(i) as Cursor
                        datos += "Última actualización: " + cursor.getString(cursor.getColumnIndexOrThrow("update")) + "\n"
                        datos += cursor.getString(cursor.getColumnIndexOrThrow("code")) + "\t" +
                                cursor.getString(cursor.getColumnIndexOrThrow("value")) + "\n"
                    } else {
                        datos += cursor.getString(cursor.getColumnIndexOrThrow("code")) + "\t" +
                                cursor.getString(cursor.getColumnIndexOrThrow("value")) + "\n"
                    }
                }
                list.append(datos)

                while (moveToNext()){
                    Log.i("MONEDAXClienteLC", " id: ${getInt(0)} , code: ${getString(1)}, moneda ${getString(2)}")
                }
            }
        }
    }

    lateinit var list : TextView

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = findViewById(R.id.jsonText)

        LoaderManager.getInstance(this)
            .initLoader(1001, null, Callbacks)

        val micursor   = contentResolver.query(
            Uri.parse("content://com.example.proyectodivisa/monedas"),
            arrayOf("_ID","base_code","update","code","value"),
            null, null,null
        )
        micursor?.apply {
            while (moveToNext()){
                Log.i("MONEDAXCliente", " id: ${getInt(0)} , code: ${getString(1)}, moneda ${getString(2)}")
            }
        }

    }
}