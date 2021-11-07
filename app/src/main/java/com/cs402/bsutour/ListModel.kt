package com.cs402.bsutour

import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Parcelize
public data class TourLocation(var name: String, var selected: Boolean, var Location: String) : Parcelable

public class ListModel: ArrayList<TourLocation>()   {


    val SimpleTourList = arrayListOf<TourLocation>(
        TourLocation("1.\tStudent Union Building",false, "43.60141111,-116.20187222"),
        TourLocation("2.\tRec Center",false, "43.60053889,-116.20010000"),
        TourLocation("3.\tAlbertsons Stadium",false, "43.60198889,-116.19707778"),
        TourLocation("4.\tFirst-Year Village",false, "43.60398611,-116.19969444"),
        TourLocation("5.\tAlbertsons Library",false, "43.60393611,-116.20353333"),
        TourLocation("6.\tThe Quad",false, "43.60429444,-116.20435000"),
        TourLocation("7.\tFriendship Bridge",false, "43.60509444,-116.20379444"),
        TourLocation("8.\tInteractive learning Center",false, "43.60494167,-116.20614444"),
        TourLocation("9.\tCenter for Visual Arts",false, "43.60625833,-116.20926667"),
        TourLocation("10.\tMicron Business and Economics Building",false, "43.60567778,-116.20950000"),
        TourLocation("11.\tB Plaza",false, "43.60341111,-116.20494444"),
    )

    val CampusFoodList = arrayListOf<TourLocation>(
        TourLocation("1.\tAlbertsons Library",false, "43.60393611,-116.20353333"),
    )

    val ComputerLabList = arrayListOf<TourLocation>(
        TourLocation("1.\tStudent Union Building",false, "43.60141111,-116.20187222"),
    )


    var ActiveList = SimpleTourList

    init {
        val nst = Executors.newSingleThreadExecutor()
        nst.execute {
            val length = ActiveList.size;

            for (i in 0 until length) {
                add(ActiveList[i])
            }
        }
    }



}
