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
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var camp: CameraPosition
    private var newmap = true
    private var locationUpdateState = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        // 3 request code passes to on activity result
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    val LocationList = ListModel()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("StateChange", "enterOnCreate")


        kRecyclerView =
            findViewById(R.id.Practice_recycler_view) as RecyclerView
        kRecyclerView.layoutManager = LinearLayoutManager(this)


        //insert adapter here
        var kadapter: KAdapter = KAdapter(this, LocationList)

        kRecyclerView.setAdapter(kadapter)


        if (savedInstanceState != null) {
            if(savedInstanceState.containsKey("myPosition")) {
                camp = savedInstanceState.getParcelable<CameraPosition>("myPosition")!!
                newmap = false
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
        map.getUiSettings().setZoomControlsEnabled(true)
        map.getUiSettings().setMapToolbarEnabled(false);
        map.cameraPosition
        map.setOnMarkerClickListener(this)

        val test = LocationList.size

        for(i in 0 until LocationList.size) {
            val latlongf = LocationList[i].Location
            val latlong = latlongf.split(",")
            val latitude = latlong[0].toDouble()
            val longitude = latlong[1].toDouble()
            var pos = LatLng(latitude, longitude)
            if(LocationList[i].visited){
            }


            var j = i +1
            map.addMarker(
                if(LocationList[i].visited){
                    MarkerOptions()
                        .position(pos)
                        .title(j.toString())
                        .snippet(LocationList[i].name.substringAfterLast("."))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                } else{
                    MarkerOptions()
                        .position(pos)
                        .title(j.toString())
                        .snippet(LocationList[i].name.substringAfterLast("."))
                        .icon(BitmapDescriptorFactory.defaultMarker(15f))
                }


            )
        }



        if (newmap) {
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
        } else{
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

// Add check to see if they are on campus, otherwise go to defualt location. geo fences?

                //map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16.5f))
            }
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        return false
    }


    //Not currently working, several methods depreciated
/*    private fun startLocationUpdates() {
        //1
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        //2
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null *//* Looper *//*)
    }*/

    /*private fun createLocationRequest() {
        // 1
        locationRequest = LocationRequest.create()
        // 2
        locationRequest.interval = 10000
        // 3
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        // 4
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        // 5
        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // 6
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(this@MainActivity ,
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }*/



//    onActivityResult depreciated, need to find alternative
    // 1
/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }*/

// LISTS for locations


    fun onDestory() {
        super.onDestroy()
        Log.d("StateChange","enterOnDestroy")
    }

    // 2
    override fun onPause() {
        super.onPause()
//        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d("StateChange","enterOnPause")
    }

    // 3
    public override fun onResume() {
        super.onResume()
        /*if (!locationUpdateState) {
            startLocationUpdates()
        }*/
        Log.d("StateChange", "EnterOnResume")
    }



    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        Log.d("StateChange", "enterOnSaveInstanceState")
        // in here we need to save out the data model as a bundle


        outState.putParcelable("myPosition", map.cameraPosition)
/*        //save current map state
        Log.d("Saving", "saved current location in bundle")

        outState.putDouble("long", )
        outState.putDouble("lat", )
        outState.putFloat("Zoom", "")


        //saving the entire data model!!!!!
        outState.putParcelable("Places", LocationList)*/


        //Log.d("BSUTour", "SAVED Locationlist in bundle")
        //outState.putPar



    }




}