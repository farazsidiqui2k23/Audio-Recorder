package com.example.audiorecorder.RoomSetup

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Duration


@Entity(tableName = "AudioRecordings")
data class Audio(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val audio_Name: String,
    val audio_Path: String,
    val time_Stamp: Long = System.currentTimeMillis(),
    val duration: Long = 0
)
