package com.example.mediaplayer.ui.player;

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MusicService : Service() {

    companion object {
        private const val DURATION_FORMAT = "%d:%02d"
    }

    private val myBinder = MyLocalBinder()

    private var mediaPlayer: MediaPlayer? = null
    private var uri: Uri? = null
    private var timer: ScheduledExecutorService? = null

    private var positionLiveData = MutableLiveData<String>()
    val position = positionLiveData

    private var currentDurationLiveData = MutableLiveData<String>()
    val currentDuration = currentDurationLiveData

    private var seekBarLiveData = MutableLiveData<Int>()
    val seekBar = seekBarLiveData

    private var stopPlayerLiveData = MutableLiveData<Boolean>()
    val stopPlayer = stopPlayerLiveData

    override fun onBind(p0: Intent?): IBinder? {
        return myBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        uri = intent?.data
        createMediaPlayer(uri!!)
        return START_STICKY
    }

    private fun createMediaPlayer(uri: Uri) {
        mediaPlayer = MediaPlayer.create(applicationContext, uri)
        try {
            mediaPlayer!!.setOnCompletionListener {
                releaseMediaPlayer()
                stopSelf()
            }
            currentDurationLiveData.postValue(getTime(mediaPlayer!!.duration.toLong()))
            seekBarLiveData.postValue(mediaPlayer!!.duration)
        } catch (e: IOException) {
        }
    }

    private fun releaseMediaPlayer() {
        timer?.shutdown()
        if (mediaPlayer != null) {
            mediaPlayer!!.release();
            mediaPlayer = null
        }
        stopPlayer.postValue(true)
    }

    fun playPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                timer!!.shutdown()
            } else {
                it.start()
                timer = Executors.newScheduledThreadPool(1)
                (timer as ScheduledExecutorService).scheduleAtFixedRate(Runnable {
                    if (mediaPlayer != null) {
                        seekBarLiveData.postValue(it.currentPosition)
                    }
                }, 10, 10, TimeUnit.MILLISECONDS)
            }
        }
    }

    fun updatePlayingTime() {
        mediaPlayer?.let {
            val millis = it.currentPosition
            positionLiveData.postValue(getTime(millis.toLong()))
            var duration = (it.duration - millis)
            currentDurationLiveData.postValue(getTime(duration.toLong()))
        }
    }

    private fun getTime(millis: Long): String {
        val total_sec = TimeUnit.SECONDS.convert(millis, TimeUnit.MILLISECONDS)
        val min = TimeUnit.MINUTES.convert(total_sec, TimeUnit.SECONDS)
        val secs = total_sec - min * 60
        return String.format(Locale.ENGLISH, DURATION_FORMAT, min, secs)
    }

    fun seekMediaPlayer(progress: Int) {
        mediaPlayer?.let { it.seekTo(progress) }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.let { it.stop() }
    }

    inner class MyLocalBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

}
