package com.cs402.bsutour

import android.os.Parcelable

// This returns a list of locations that outline the perimeter of BSU campus.
// We will use this with a geofence to have the start location use your current
// location if you are inside the boundary or Student union if you are outside of it.

public data class Location(var name: String, var latitude: String, var longitude: String)

class Perimeter
{

    fun boundary(): List<Location>
    {
        return listOf(
            Location("Point 1","43.604556", "-116.193397"),
            Location("Point 2","43.598547", "-116.193500"),
            Location("Point 3","43.598367", "-116.203619"),
            Location("Point 4","43.605572", "-116.211411"),
            Location("Point 5","43.609225", "-116.207939")
        )
    }

}