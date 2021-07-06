package com.talentica.androidkotlin.videostreaming

import android.app.ProgressDialog
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import kotlin.concurrent.fixedRateTimer

/**
 * Created by suyashg on 01/06/17.
 */

//Always device to run this App
class MainActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener,
        View.OnClickListener {

    private val HLS_STREAMING_SAMPLE = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"
    private var sampleVideoView: VideoView? = null
    private var seekBar: SeekBar? = null
    private var playPauseButton: ImageView? = null
    private var stopButton: ImageView? = null
    private var runningTime: TextView? = null
    private var currentPosition: Int = 0
    private var isRunning = false

    //Always device to run this App
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sampleVideoView = findViewById<VideoView>(R.id.videoView)
        sampleVideoView?.setVideoURI(Uri.parse(HLS_STREAMING_SAMPLE))

        playPauseButton = findViewById<ImageView>(R.id.playPauseButton)
        playPauseButton?.setOnClickListener(this)

        stopButton = findViewById<ImageView>(R.id.stopButton)
        stopButton?.setOnClickListener(this)

        seekBar = findViewById<SeekBar>(R.id.seekBar)
        seekBar?.setOnSeekBarChangeListener(this)

        runningTime = findViewById<TextView>(R.id.runningTime)
        runningTime?.setText("00:00")

        Toast.makeText(this, "Buffering...Please wait", Toast.LENGTH_LONG).show()

        //Add the listeners
        sampleVideoView?.setOnCompletionListener(this)
        sampleVideoView?.setOnErrorListener(this)
        sampleVideoView?.setOnPreparedListener(this)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Toast.makeText(baseContext, "Play finished", Toast.LENGTH_LONG).show()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Log.e("video", "setOnErrorListener ")
        return true
    }

    override fun onPrepared(mp: MediaPlayer?) {
        seekBar?.setMax(sampleVideoView?.getDuration()!!)
        sampleVideoView?.start()

        val fixedRateTimer = fixedRateTimer(name = "hello-timer",
                initialDelay = 0, period = 1000) {
            refreshSeek()
        }

        playPauseButton?.setImageResource(R.mipmap.pause_button)
    }

    fun refreshSeek() {
        seekBar?.setProgress(sampleVideoView?.getCurrentPosition()!!);

        if (sampleVideoView?.isPlaying()!! == false) {
            return
        }

        var time = sampleVideoView?.getCurrentPosition()!! / 1000;
        var minute = time / 60;
        var second = time % 60;

        runOnUiThread {
            runningTime?.setText(minute.toString() + ":" + second.toString());
        }
    }

    var refreshTime = Runnable() {
        fun run() {

        }
    };

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        //do nothing
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        //do nothing
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        sampleVideoView?.seekTo(seekBar?.getProgress()!!)
    }

    override fun onClick(v: View?) {
        if (v?.getId() == R.id.playPauseButton) {
            //Play video
            if (!isRunning) {
                isRunning = true
                sampleVideoView?.resume()
                sampleVideoView?.seekTo(currentPosition)
                playPauseButton?.setImageResource(R.mipmap.pause_button)
            } else { //Pause video
                isRunning = false
                sampleVideoView?.pause()
                currentPosition = sampleVideoView?.getCurrentPosition()!!
                playPauseButton?.setImageResource(R.mipmap.play_button)
            }
        } else if (v?.getId() == R.id.stopButton) {
            playPauseButton?.setImageResource(R.mipmap.play_button)
            sampleVideoView?.stopPlayback()
            currentPosition = 0
        }
    }
}