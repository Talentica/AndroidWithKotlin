package com.talentica.androidkotlin.audioplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.talentica.androidkotlin.audioplayer.utils.LogHelper
import com.talentica.androidkotlin.audioplayer.utils.SeekBarHandler


class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, OnClickListener {

    private val TAG = LogHelper.makeLogTag("MainActivity")
    private var mMediaPlayer: MediaPlayer? = null
    private var mPlayPauseButton: ImageButton? = null
    private var mSeekbar:SeekBar? = null
    private var mTimer:TextView? = null
    private var seekBarHandler:SeekBarHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSeekbar = findViewById<SeekBar>(R.id.progressbar)
        mSeekbar?.setOnSeekBarChangeListener(this)

        mPlayPauseButton = findViewById<ImageButton>(R.id.play_pause_btn)
        mPlayPauseButton?.setOnClickListener(this)

        mTimer = findViewById<TextView>(R.id.tv_progress)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.play_pause_btn) {
            togglePlayback()
        }
    }

    fun togglePlayback() {
        if (mMediaPlayer?.isPlaying == true) {
            pauseAudio()
        } else {
            createMediaPlayerIfNeeded()
            playAudio()
        }
    }

    /**
     * Makes sure the media player exists and has been reset. This will create
     * the media player if needed, or reset the existing media player if one
     * already exists.
     */
    private fun createMediaPlayerIfNeeded() {
        Log.d(TAG, "createMediaPlayerIfNeeded")
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.simplerock)

            // Make sure the media player will acquire a wake-lock while
            // playing. If we don't do that, the CPU might go to sleep while the
            // song is playing, causing playback to stop.
            mMediaPlayer?.setWakeMode(this.getApplicationContext(),
                    PowerManager.PARTIAL_WAKE_LOCK)

            // we want the media player to notify us when it's ready preparing,
            // and when it's done playing:
            mMediaPlayer?.setOnPreparedListener(this)
            mMediaPlayer?.setOnCompletionListener(this)
            mMediaPlayer?.setOnErrorListener(this)
            mMediaPlayer?.setOnSeekCompleteListener(this)
        }
    }

    fun playAudio() {
        mMediaPlayer?.start()
        seekBarHandler = SeekBarHandler(mSeekbar, mMediaPlayer, isViewOn = true, timer = mTimer!!)
        seekBarHandler?.execute()
        val pauseDrawabale = ContextCompat.getDrawable(this, android.R.drawable.ic_media_pause)
        mPlayPauseButton?.setImageDrawable(pauseDrawabale)
    }

    fun pauseAudio() {
        seekBarHandler?.cancel(true)
        mMediaPlayer?.pause()
        val playDrawabale = ContextCompat.getDrawable(this, android.R.drawable.ic_media_play)
        mPlayPauseButton?.setImageDrawable(playDrawabale)
    }

    /**
     * Releases resources used by the service for playback. This includes the
     * "foreground service" status, the wake locks and possibly the MediaPlayer.

     * @param releaseMediaPlayer Indicates whether the Media Player should also
     * *            be released or not
     */
    private fun relaxResources(releaseMediaPlayer: Boolean) {
        LogHelper.d(TAG, "relaxResources. releaseMediaPlayer=", releaseMediaPlayer)

        seekBarHandler?.cancel(true)
        seekBarHandler = null
        // stop and release the Media Player, if it's available
        if (releaseMediaPlayer && mMediaPlayer != null) {
            mMediaPlayer?.reset()
            mMediaPlayer?.release()
            mMediaPlayer = null
        }
    }

    override fun onPause() {
        super.onPause()
        if (mMediaPlayer?.isPlaying == true) {
            pauseAudio()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        relaxResources(true)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        relaxResources(true)
        val playDrawabale = ContextCompat.getDrawable(this, android.R.drawable.ic_media_play)
        mPlayPauseButton?.setImageDrawable(playDrawabale)
        mSeekbar?.progress = 0
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return false
    }

    override fun onPrepared(mp: MediaPlayer?) {
    }

    override fun onSeekComplete(mp: MediaPlayer?) {
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            val progress = seekBar?.progress
            mMediaPlayer?.seekTo(progress!!)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}
