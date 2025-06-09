package com.example.audiorecorder.Recorder

import android.os.Handler
import android.os.Looper

class Timer(private val listener: OnTimerTickListener) {

    interface OnTimerTickListener {
        fun onTimerTick(duration: String)
    }

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    private var duration = 0L
    private val delay = 100L



    fun start() {
        runnable = object : Runnable {
            override fun run() {
                duration += delay
                handler.postDelayed(this, delay)
                listener.onTimerTick(format())
            }
        }
        handler.postDelayed(runnable!!, delay)
    }

    fun stop() {
        runnable?.let { handler.removeCallbacks(it) }
        duration = 0L
        listener.onTimerTick(format()) // Update UI with reset value
    }

     fun format(): String {
        val millis = (duration % 1000) / 10  // make millis 2 digits
        val seconds = (duration / 1000) % 60
        val minutes = (duration / (1000 * 60)) % 60
        val hours = (duration / (1000 * 60 * 60))

        return if (hours > 0) {
            String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, millis)
        } else {
            String.format("%02d:%02d.%02d", minutes, seconds, millis)
        }
    }

    fun getTime(): String {
        return format()
    }


}
