package com.cs402.bsutour

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


public class KAdapter(context: Context, var practice: ArrayList<String>, var spractice: ArrayList<Boolean>)
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
            titleTextView.text = apractice

            var sscolor = "#ffffff"
            if (spractice[position]) {
                sscolor = "#cccccc"
            }
            titleTextView.setBackgroundColor(Color.parseColor(sscolor))

        }
    }
    //inner class important
    inner class PracticeHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
        val titleTextView: TextView = view.findViewById(R.id.practice_name)
        var kSelect: Boolean = false

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            //toggle selection, color shows whats selected
            var apos = getBindingAdapterPosition()

            kSelect = spractice[apos]
            kSelect = !kSelect  //flip boolean when clicked on
            spractice[apos] = kSelect // save selectness
            var sscolor = "#ffffff"
            if (kSelect) {
                sscolor = "#cccccc"
            }
            titleTextView.setBackgroundColor(Color.parseColor(sscolor))

        }
    }

}