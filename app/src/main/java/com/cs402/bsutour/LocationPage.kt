package com.cs402.bsutour


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.graphics.drawable.Drawable


class LocationPage : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_page)

        val location = intent.getStringExtra("location_summary")
        val image = intent.getStringExtra("image")

        val textView = findViewById<TextView>(R.id.textView).apply{
            text = location
        }




        val uri = "@drawable/$image" // where myresource (without the extension) is the file
        val imageResource = resources.getIdentifier(uri, null, packageName)

        val imageView = findViewById<ImageView>(R.id.imageView).apply {
            setImageResource(imageResource)
        }



        val lastpage = Intent(this, MainActivity::class.java)

        val backButton = findViewById<Button>(R.id.button)

        // Move to previous activty
        backButton.setOnClickListener { v: View? ->
            startActivity(lastpage)
        }


    }
}