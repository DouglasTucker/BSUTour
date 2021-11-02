package com.cs402.bsutour

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var kRecyclerView: RecyclerView
    val PracticeList = arrayListOf<String>("Mercury", "Venus","Earth","Mars", "Jupiter", "Saturn")
    val SPracticeList = arrayListOf<Boolean>(false, false,false,false, false, false)
    var acount = 0;
    var joinstring = ""
    var initposition = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        kRecyclerView =
            findViewById(R.id.Practice_recycler_view) as RecyclerView
        kRecyclerView.layoutManager = LinearLayoutManager(this)

        //insert adapter here
        val kadapter: KAdapter = KAdapter(this, PracticeList, SPracticeList)

        kRecyclerView.setAdapter(kadapter)

        val button: Button = findViewById(R.id.addbutton)
        //set on-click listener
        button.setOnClickListener {
            // build alert dialog
            val dialogBuilder = AlertDialog.Builder(this)
            val m_Text = ""
            val input = EditText(this)
            input.setHint("Enter Text")
            input.inputType = InputType.TYPE_CLASS_TEXT
            dialogBuilder.setView(input)

                .setCancelable(false)

                .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, which ->
                    var m_Text = input.text.toString()
                    if (m_Text.isNullOrEmpty()) {
                        acount += 1
                        m_Text = "Item $acount"
                    }
                    SPracticeList.add(false)
                    PracticeList.add(m_Text)
                    kadapter.notifyDataSetChanged()
                })

                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            val alert = dialogBuilder.create()
            alert.setTitle("Enter new item")

            alert.show()

        }

        val rembutton: Button = findViewById(R.id.rembutton)
        //set on-click listener
        rembutton.setOnClickListener {
            for (i in SPracticeList.size downTo 1) {
                if(SPracticeList[i-1]) {
                    SPracticeList.removeAt(i-1)
                    PracticeList.removeAt(i-1)
                }
            }
            kadapter.notifyDataSetChanged()
        }

        val joinbutton: Button = findViewById(R.id.joinbutton)
        //set on-click listener
        joinbutton.setOnClickListener {

            for (i in SPracticeList.size downTo 1) {
                if(SPracticeList[i-1]) {
                    initposition = i-1
                    joinstring = PracticeList.get(i-1).plus(", ").plus(joinstring)
                    SPracticeList.removeAt(i-1)
                    PracticeList.removeAt(i-1)
                }
            }
            if (!joinstring.isNullOrEmpty()) {
                joinstring = joinstring.dropLast(2)
                SPracticeList.add(initposition, false)
                PracticeList.add(initposition, joinstring)
                joinstring = ""
                initposition = 0;
                kadapter.notifyDataSetChanged()
            }

            val splitbutton: Button = findViewById(R.id.splitbutton)
            //set on-click listener
            splitbutton.setOnClickListener {
                var delimeter = ", "
                for (i in SPracticeList.size downTo 1) { // find selected items
                    if(SPracticeList[i-1]) { // if item selected
                        var parts = PracticeList[i-1].split(delimeter) //list of parts

                        if (!parts.isNullOrEmpty()){

                            for(j in parts.size downTo 1){ //adding for each part
                                SPracticeList.add(i, false)
                                PracticeList.add(i, parts[j-1])
                            }

                            SPracticeList.removeAt(i-1)
                            PracticeList.removeAt(i-1)
                        }

                    }
                }
                kadapter.notifyDataSetChanged()
            }

        }

    }
}