package com.example.audiorecorder.RoomSetup

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

//use to create CURD operation fon BD
@Dao
interface AudioDao {

    @Query("SELECT * FROM AudioRecordings")
    fun getAudio() : LiveData<List<Audio>>

    @Insert
    suspend fun saveAudio(audio: Audio)

    @Delete
    suspend fun deleteAudio(audio: Audio)

}