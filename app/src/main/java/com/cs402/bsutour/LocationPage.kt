package com.cs402.bsutour


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.util.Log
import android.view.MotionEvent
import kotlinx.android.synthetic.main.location_page_audio.*


class LocationPage : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val location = intent.getStringExtra("location_summary")
        val image = intent.getStringExtra("image")
        val latlong = intent.getStringExtra("latlong")
        val ListIndex = intent.getIntExtra("listIndex", 0)
        var toggle = 0

        if (ListIndex == 0) {
            setContentView(R.layout.location_page_audio)
        } else {
            setContentView(R.layout.location_page)
        }

        Log.d("test1", "hello1")
        val uria = "R.raw.$image" // where myresource (without the extension) is the file
        Log.d("test1", "hello2")
        val audioResource = resources.getIdentifier(uria, null, packageName)
        Log.d("test1", "hello3")
        mediaPlayer = MediaPlayer.create(this, R.raw.t1 )
        Log.d("test1", "hello4")
        mediaPlayer?.setOnPreparedListener {
            println("READY TO GO")
        }
        Log.d("test1", "hello5")
 /*       pushButton.setOnTouchListener { _, event ->
            handleTouch(event)
            true
        }
*/

        val textView = findViewById<TextView>(R.id.textView).apply {
            text = location
        }


        val uri = "@drawable/$image" // where myresource (without the extension) is the file
        val imageResource = resources.getIdentifier(uri, null, packageName)

        val imageView = findViewById<ImageView>(R.id.imageView).apply {
            setImageResource(imageResource)
        }

        val backButton = findViewById<Button>(R.id.button)

        // Move to previous activty
        backButton.setOnClickListener { v: View? ->
            if(toggle == 0){
                mediaPlayer?.seekTo(0)
                mediaPlayer?.pause()
            }
            finish()

        }

        if (ListIndex == 0) {
            var playButton = findViewById(R.id.pushButton) as Button
            playButton.setOnClickListener {
                if(toggle == 0 ){
                    toggle = 1
                    mediaPlayer?.start()
                    playButton.text = "pause"
                } else{
                    toggle = 0
                    playButton.text = "start"
                    mediaPlayer?.pause()
                }
            }

            var restartButton = findViewById(R.id.restart_button) as Button
            restartButton.setOnClickListener {
                toggle = 1
                playButton.text = "pause"
                mediaPlayer?.seekTo(0)
                mediaPlayer?.start()
            }
        }

    }




/*
    private fun handleTouch(event: MotionEvent?) {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                println("down")
                mediaPlayer?.start()
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                println("up or cancel")
                mediaPlayer?.pause()
                mediaPlayer?.seekTo(0)
            }
            else -> {
                println("other")
            }
        }
    }*/
}

















