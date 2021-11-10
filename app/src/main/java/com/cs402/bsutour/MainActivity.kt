package com.cs402.bsutour

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.util.Log
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import kotlinx.android.parcel.Parcelize
import androidx.activity.result.ActivityResultCallback

import androidx.activity.result.contract.ActivityResultContracts

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.google.android.gms.maps.model.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var kRecyclerView: RecyclerView
    lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var camp: CameraPosition
    private var newmap = true
    lateinit var geofencingClient: GeofencingClient

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


    var LocationList = ListModel()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("StateChange", "enterOnCreate")


        geofencingClient = LocationServices.getGeofencingClient(this)

        kRecyclerView =
            findViewById(R.id.Practice_recycler_view) as RecyclerView
        kRecyclerView.layoutManager = LinearLayoutManager(this)


        //insert adapter here
        var kadapter: KAdapter = KAdapter(this, LocationList)

        kRecyclerView.setAdapter(kadapter)



        /*option = findViewById(R.id.tourselect) as Spinner

        val options = arrayOf("Simple Tour", "Campus Food", "Computer labs")
        option.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, options)

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                TODO()
            }
        }*/



        // Load saved data
        if (savedInstanceState != null) {
            if(savedInstanceState.containsKey("myPosition")) {
                camp = savedInstanceState.getParcelable<CameraPosition>("myPosition")!!
                newmap = false //on map ready now knows to load save instead of default.
            }
        }


        //map stuff

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }




    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        //enable zoom and disables directions
        map.getUiSettings().setZoomControlsEnabled(true)
        map.getUiSettings().setMapToolbarEnabled(false);

        map.setOnMarkerClickListener(this)

        //goes through model list and adds markers to map
        for(i in 0 until LocationList.size) {
            val latlongf = LocationList[i].Location
            val latlong = latlongf.split(",")
            val latitude = latlong[0].toDouble()
            val longitude = latlong[1].toDouble()
            var pos = LatLng(latitude, longitude)


            var j = i +1   //accounts for difference in index and location number
            map.addMarker(
                if(LocationList[i].visited){ //if location visited mark green complete
                    MarkerOptions()
                        .position(pos)
                        .title(j.toString())
                        .snippet(LocationList[i].name.substringAfterLast("."))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                } else{ //if not visited mark orange
                    MarkerOptions()
                        .position(pos)
                        .title(j.toString())
                        .snippet(LocationList[i].name.substringAfterLast("."))
                        .icon(BitmapDescriptorFactory.defaultMarker(15f))
                }
            )
        }



        if (newmap) { //check if this is a new map, if so load default location
            // starting Location, student union building
            val latitude = 43.60141111
            val longitude = -116.20187222

            //set starting location for when map loads
            val startLocation = LatLng(latitude, longitude)
            val zoomlevel = 16.5f

            // [START_EXCLUDE silent]
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation,zoomlevel))
            //map.animateCamera( CameraUpdateFactory.zoomTo( 16.5f ) );
            // [END_EXCLUDE]
        } else{ // if you have had map open before then reload saved position
            map.moveCamera(CameraUpdateFactory.newCameraPosition(camp))
        }
        setUpMap() // special
    }

    // MAP STUFF

    //check if app already has permission to use user location, if not it prompts user for permission
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location //get last known location
                val currentLatLng = LatLng(location.latitude, location.longitude)

// Add check to see if they are on campus, otherwise go to defualt location. geo fences????

                //map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16.5f))
            }
        }
    }

    // will need to give option to go to activity screen/play audio for markers location
    override fun onMarkerClick(p0: Marker): Boolean {

        return false
    }


    fun onDestory() {
        super.onDestroy()
        Log.d("StateChange","enterOnDestroy")
    }


    override fun onPause() {
        super.onPause()
        Log.d("StateChange","enterOnPause")
    }


    public override fun onResume() {
        super.onResume()
        Log.d("StateChange", "EnterOnResume")
    }


    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        Log.d("StateChange", "enterOnSaveInstanceState")
        // in here we need to save out the data model as a bundle

        outState.putParcelable("myPosition", map.cameraPosition)
    }





}