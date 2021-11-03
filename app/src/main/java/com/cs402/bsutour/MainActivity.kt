package com.cs402.bsutour

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var kRecyclerView: RecyclerView
    val PracticeList = arrayListOf<String>("1.\tStudent Union Building", "2.\tRec Center","3.\tAlbertsons Stadium","4.\tFirst-Year Village", "5.\tAlbertsons Library", "6.\tThe Quad","7.\tFriendship Bridge","8.\tInteractive learning Center","9.\tCenter for Visual Arts","10.\tMicron Business and Economics Building","11.\tB Plaza")
    val SPracticeList = arrayListOf<Boolean>(false, false, false, false, false, false, false, false, false, false, false)
    val LPracticeList = arrayListOf<String>( "43.60141111,-116.20187222", "43.60053889,-116.20010000", "43.60198889,-116.19707778", "43.60398611,-116.19969444", "43.60393611,-116.20353333", "43.60429444,-116.20435000", "43.60509444,-116.20379444", "43.60494167,-116.20614444","43.60625833,-116.20926667","43.60567778,-116.20950000","43.60341111,-116.20494444")

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

        //map stuff

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)



//        val button: Button = findViewById(R.id.addbutton)
//        //set on-click listener
//        button.setOnClickListener {
//            // build alert dialog
//            val dialogBuilder = AlertDialog.Builder(this)
//            val m_Text = ""
//            val input = EditText(this)
//            input.setHint("Enter Text")
//            input.inputType = InputType.TYPE_CLASS_TEXT
//            dialogBuilder.setView(input)
//
//                .setCancelable(false)
//
//                .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, which ->
//                    var m_Text = input.text.toString()
//                    if (m_Text.isNullOrEmpty()) {
//                        acount += 1
//                        m_Text = "Item $acount"
//                    }
//                    SPracticeList.add(false)
//                    PracticeList.add(m_Text)
//                    kadapter.notifyDataSetChanged()
//                })
//
//                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
//                        dialog, id -> dialog.cancel()
//                })
//
//            val alert = dialogBuilder.create()
//            alert.setTitle("Enter new item")
//
//            alert.show()
//
//        }

//        val rembutton: Button = findViewById(R.id.rembutton)
//        //set on-click listener
//        rembutton.setOnClickListener {
//            for (i in SPracticeList.size downTo 1) {
//                if(SPracticeList[i-1]) {
//                    SPracticeList.removeAt(i-1)
//                    PracticeList.removeAt(i-1)
//                }
//            }
//            kadapter.notifyDataSetChanged()
//        }

//        val joinbutton: Button = findViewById(R.id.joinbutton)
//        //set on-click listener
//        joinbutton.setOnClickListener {
//
//            for (i in SPracticeList.size downTo 1) {
//                if(SPracticeList[i-1]) {
//                    initposition = i-1
//                    joinstring = PracticeList.get(i-1).plus(", ").plus(joinstring)
//                    SPracticeList.removeAt(i-1)
//                    PracticeList.removeAt(i-1)
//                }
//            }
//            if (!joinstring.isNullOrEmpty()) {
//                joinstring = joinstring.dropLast(2)
//                SPracticeList.add(initposition, false)
//                PracticeList.add(initposition, joinstring)
//                joinstring = ""
//                initposition = 0;
//                kadapter.notifyDataSetChanged()
//            }
//
//            val splitbutton: Button = findViewById(R.id.splitbutton)
//            //set on-click listener
//            splitbutton.setOnClickListener {
//                var delimeter = ", "
//                for (i in SPracticeList.size downTo 1) { // find selected items
//                    if(SPracticeList[i-1]) { // if item selected
//                        var parts = PracticeList[i-1].split(delimeter) //list of parts
//
//                        if (!parts.isNullOrEmpty()){
//
//                            for(j in parts.size downTo 1){ //adding for each part
//                                SPracticeList.add(i, false)
//                                PracticeList.add(i, parts[j-1])
//                            }
//
//                            SPracticeList.removeAt(i-1)
//                            PracticeList.removeAt(i-1)
//                        }
//
//                    }
//                }
//                kadapter.notifyDataSetChanged()
//            }
//
//        }

    }

    // [END maps_marker_get_map_async]
    // [END_EXCLUDE]

    // [START maps_marker_on_map_ready_add_marker]
    override fun onMapReady(googleMap: GoogleMap) {
//        val latlongf = getIntent().getStringExtra("MARKER")
//        val latlong = latlongf.split(",")
        for(i in 1 until 12) {
            val latlongf = LPracticeList[i-1]
            val latlong = latlongf.split(",")
            val latitude = latlong[0].toDouble()
            val longitude = latlong[1].toDouble()
            var pos = LatLng(latitude, longitude)

            googleMap.addMarker(
                MarkerOptions()
                    .position(pos)
                    .title(i.toString())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            )



        }

//        val latlong = "43.60141111,-116.20187222".split(",")
//        val latitude = latlong[0].toDouble()
//        val longitude = latlong[1].toDouble()
        var StudentUnion = LatLng(43.60141111, -116.20187222)
//
//        googleMap.addMarker(
//            MarkerOptions()
//                .position(sydney)
//                .title("Student Union Building")
//        )
        // [START_EXCLUDE silent]
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(StudentUnion))
        googleMap.animateCamera( CameraUpdateFactory.zoomTo( 16.5f ) );
        // [END_EXCLUDE]
    }
    // [END maps_marker_on_map_ready_add_marker]
}