package com.cs402.bsutour


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.media.MediaPlayer
import android.util.Log


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

        val ura = "@raw/$image"
        val audioResource = resources.getIdentifier(ura, null, packageName)
        mediaPlayer = MediaPlayer.create(this,audioResource)
        mediaPlayer?.setOnPreparedListener {
            println("READY TO GO")
        }

        val textView = findViewById<TextView>(R.id.textView).apply {
            text = location
        }

        val uri = "@drawable/$image" // where myresource (without the extension) is the file
        val imageResource = resources.getIdentifier(uri, null, packageName)
        Log.d("test3", imageResource.toString())
        Log.d("test3", R.drawable.t1.toString())
        val imageView = findViewById<ImageView>(R.id.imageView).apply {
            setImageResource(imageResource)
        }

        val backButton = findViewById<Button>(R.id.button)

        // Move to previous activty
        backButton.setOnClickListener { v: View? ->
            if(toggle == 1){
                toggle = 0
                mediaPlayer?.seekTo(0)
                mediaPlayer?.pause()
            }
            finish()

        }

        if (ListIndex == 0) {
            val playButton = findViewById(R.id.pushButton) as Button
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

            val restartButton = findViewById(R.id.restart_button) as Button
            restartButton.setOnClickListener {
                toggle = 1
                playButton.text = "pause"
                mediaPlayer?.seekTo(0)
                mediaPlayer?.start()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
        Log.d("StateChange","enterOnPause")
    }
}

















