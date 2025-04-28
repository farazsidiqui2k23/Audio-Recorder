package com.example.audiorecorder.Recorder

import android.net.Uri
import java.io.File

interface AudioRecorder {
    fun start(outputFile : File)
    fun stop()

}