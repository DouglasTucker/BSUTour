package com.cs402.bsutour

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.model.*
import android.widget.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var kRecyclerView: RecyclerView
    lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var pmap: CameraPosition //previous map
    private var newmap = true
    lateinit var geofencingClient: GeofencingClient
    private val ShowGeoFencePerimeter = true


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    var LocationList = ListModel(0)
    var listIndex = 0
    //list of boundary lat/long
    val BoundaryList = Perimeter().boundary()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_horizontal)
        } else {
            setContentView(R.layout.activity_main)
        }

        Log.d("StateChange", "enterOnCreate")

        listIndex = intent.getIntExtra("listy", 0)
        LocationList = ListModel(listIndex)
        geofencingClient = LocationServices.getGeofencingClient(this)

        kRecyclerView =
            findViewById(R.id.Practice_recycler_view) as RecyclerView
        kRecyclerView.layoutManager = LinearLayoutManager(this)

        //insert adapter here
        var kadapter: KAdapter = KAdapter(this, LocationList)

        kRecyclerView.setAdapter(kadapter)

        val option = findViewById(R.id.tourselect) as Spinner
        val options = arrayOf("Simple Tour", "Campus Food", "Computer labs")

        val intent = Intent(this, MainActivity::class.java)

        option.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, options)
        option.setSelection(listIndex)
        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(listIndex != position){
                    listIndex = position
                    Log.d("Test", "Here we go again $listIndex")
                    intent.putExtra("listy", listIndex)
                    finish()
                    startActivity(intent)
                }

            }
        }



        // Load saved data
        if (savedInstanceState != null) {
            if(savedInstanceState.containsKey("myPosition")) {
                pmap = savedInstanceState.getParcelable<CameraPosition>("myPosition")!!
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

        //enable zoom and disables get directions
        map.getUiSettings().setZoomControlsEnabled(true)
        map.getUiSettings().setMapToolbarEnabled(false)

        //creates a click listener on makers
        map.setOnMarkerClickListener(this)



        enableUserLocation()

        //check circle shows approximate location of geo fence for start location
        if (ShowGeoFencePerimeter){
            val circle = googleMap.addCircle(
                CircleOptions()
                    .center(LatLng(  43.603842, -116.203303))
                    .radius(700.0)
                    .strokeColor(Color.RED)
                    .strokeWidth(4f)
            )
        }

        //goes through model list and adds markers to map
        for(i in 0 until LocationList.size) {
            val latlongf = LocationList[i].Location
            val latlong = latlongf.split(",")
            val latitude = latlong[0].toDouble()
            val longitude = latlong[1].toDouble()
            var pos = LatLng(latitude, longitude)
            var j = i +1   //accounts for difference in index and location number


            var uri = "@drawable/m$j"

            if(LocationList[i].visited){
                uri = "@drawable/v$j"
            }



            val imagemarker = resources.getIdentifier(uri, null, packageName)
            //val ico = BitmapFactory.decodeResource(this.getResources(), imagemarker)


            var marker = map.addMarker(

                if(LocationList[i].visited){ //if location visited mark green complete
                    MarkerOptions()
                        .position(pos)
                        .alpha(0.6f) //transparincy
                        .title(j.toString())
                        .snippet(LocationList[i].name.substringAfterLast("."))
                        .icon(BitmapDescriptorFactory.fromResource(imagemarker))
                        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                } else{ //if not visited mark orange
                    MarkerOptions()
                        .position(pos)
                        .title(j.toString())
                        .snippet(LocationList[i].name.substringAfterLast("."))
                        .icon(BitmapDescriptorFactory.fromResource(imagemarker))
                        //.icon(BitmapDescriptorFactory.defaultMarker(15f))
                }
            )

            marker?.tag = LocationList[i]

            //add geo fence



        } //end for loop for addign markers


        if (newmap) { //check if this is a new map, if so load default location

            // starting Location, student union building
            val latitude = 43.60141111
            val longitude = -116.20187222
            val startLocation = LatLng(latitude, longitude)
            val zoomlevel = 16.5f

            // move camera to starting location of student union
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation,zoomlevel))

            setUpMap() // checks if on campus
        } else{ // if you have had map open before then reload saved position
            map.moveCamera(CameraUpdateFactory.newCameraPosition(pmap))
        }

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
            // Get last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location //get last known location
                val currentLatLng = LatLng(location.latitude, location.longitude)

                // Add check to see if they are on campus, otherwise go to defualt location. diy geofence
                if(isInsideCircle(location.latitude, location.longitude, 43.603842, -116.203303, 0.008))
                {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16.5f))
                }
            }
        }
    }


    fun enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        map.isMyLocationEnabled = true
    }


    // will need to give option to go to activity screen/play audio for markers location
    override fun onMarkerClick(marker: Marker): Boolean {
        val location = marker.tag as? TourLocation
        /*        Toast.makeText(
            this,
            "${marker.title} has been clicked. ${location?.description}",
            Toast.LENGTH_SHORT
        ).show()*/

        val intent = Intent(this, LocationPage::class.java).apply {
            putExtra("location_summary", location?.description)
            putExtra("image", location?.image)
            putExtra("latlong", location?.Location)
            putExtra("listIndex", listIndex)
        }


        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("Do you want to view location info page")
            .setCancelable(false)
            .setPositiveButton("Proceed", DialogInterface.OnClickListener{
                dialog, id -> startActivity(intent)
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener{
                dialog, id -> dialog.cancel()
            })
        if (listIndex == 0) {
            val alert = dialogBuilder.create()
            alert.setTitle("Learn More!")
            alert.show()
        }

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
        outState.putParcelableArrayList("PLACES", LocationList )
    }


    fun LaunchLocation(){
        val intent = Intent(this, LocationPage::class.java).apply {
           // putExtra("position", x )
        }
        startActivity(intent)
    }


    fun isInsideCircle(x: Double, y: Double, circleX: Double, circleY: Double, circleRadius: Double): Boolean
    {
        val absX = Math.pow(Math.abs(x - circleX).toDouble(), 2.0)
        val absY = Math.pow(Math.abs(y - circleY).toDouble(), 2.0)
        return Math.sqrt(absX + absY) < circleRadius }




}



