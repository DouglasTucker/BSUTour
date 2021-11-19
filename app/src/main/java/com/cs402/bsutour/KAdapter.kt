package com.cs402.bsutour

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.gms.maps.CameraUpdateFactory


public class KAdapter(context: Context, var practice: ArrayList<TourLocation>)
    : RecyclerView.Adapter<KAdapter.PracticeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : KAdapter.PracticeHolder {
        val view = LayoutInflater.from(parent.getContext()).inflate(R.layout.practice_item_view, parent, false)
        return PracticeHolder(view)
    }

    override fun getItemCount() = practice.size

    override fun onBindViewHolder(holder: PracticeHolder, position: Int) {

        val apractice = practice[position]
        holder.apply {
            titleTextView.text = apractice.name

            var sscolor = "#ffffff"
            if (practice[position].selected) {
                sscolor = "#cccccc"
            }
            titleTextView.setBackgroundColor(Color.parseColor(sscolor))
        }

    }
    //inner class important
    inner class PracticeHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        val titleTextView: TextView = view.findViewById(R.id.practice_name)
        var kSelect: Boolean = false

        init {
            itemView.setOnClickListener(this)
        }



        override fun onClick(v: View) {
            //toggle selection, color shows whats selected

            var apos = getBindingAdapterPosition()

            kSelect = practice[apos].selected
            kSelect = !kSelect  //flip boolean when clicked on
            practice[apos].selected = kSelect // save selectness
            var sscolor = "#ffffff"
            if (kSelect) {
                sscolor = "#cccccc"
            }
            titleTextView.setBackgroundColor(Color.parseColor(sscolor))

            if(!kSelect){
                var intent = Intent(v.getContext(), LocationPage::class.java)
                intent.putExtra("location_summary", practice[apos].description)
                intent.putExtra("image", practice[apos].image)
                intent.putExtra("latlong", practice[apos].Location)

                if(practice[apos].image.first() == 't'){
                    intent.putExtra("listIndex", 0)
                } else{
                    intent.putExtra("listIndex", 1)
                }


                v.getContext().startActivity(intent)
            }


           /* val intent = Intent(this, LocationPage::class.java).apply {
                putExtra("location_summary", location?.description)
                putExtra("image", location?.image)
            }
            startActivity(intent)*/

        }

        override fun onLongClick(v: View?): Boolean {
            var apos = getBindingAdapterPosition()


/*            var intent = Intent(v?.getContext(), LocationPage::class.java)
            intent.putExtra("location_summary", practice[apos].description)
            intent.putExtra("image", practice[apos].image)
            intent.putExtra("latlong", practice[apos].Location)
            if (v != null) {
                v.getContext().startActivity(intent)
            }*/
            return false
        }
    }
}