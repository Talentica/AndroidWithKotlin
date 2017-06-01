package com.talentica.androidkotlin.audioplayer.utils

import android.media.MediaPlayer
import android.os.AsyncTask
import android.widget.SeekBar
import android.widget.TextView

/**
 * Created by suyashg on 29/05/17.
 */

class SeekBarHandler(val seekbar: SeekBar?, var mediaPlayer: MediaPlayer?, var isViewOn: Boolean,val timer:TextView): AsyncTask<Void, Void, Boolean>() {

    override fun onPreExecute() {
        super.onPreExecute()
        seekbar?.max = mediaPlayer?.duration!!
    }

    override fun onProgressUpdate(vararg values: Void?) {
        super.onProgressUpdate(*values)
        val time = mediaPlayer?.getCurrentPosition()!!
        seekbar?.setProgress(time);
        var seconds = (time / 1000)
        val minutes = time / (1000 * 60) % 60
        seconds = seconds - minutes * 60
        timer.setText(minutes.toString()+":"+seconds.toString())
    }

    override fun onCancelled() {
        super.onCancelled()
        setViewOnOff(false)
    }

    fun setViewOnOff(isOn:Boolean) {
        isViewOn = isOn
    }

    fun refreshMediaPlayer(mediaPlayer: MediaPlayer?) {
        this.mediaPlayer = mediaPlayer
    }

    override fun doInBackground(vararg params: Void?): Boolean {
        while (mediaPlayer?.isPlaying() == true && isViewOn == true) {
            try {
                Thread.sleep(200)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            publishProgress()
        }
        return true
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
    }

}