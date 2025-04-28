package com.example.audiorecorder.RoomSetup

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class AudioViewModel(application: Application) : AndroidViewModel(application) {

    val audioDAO = AudioDatabase.getDatabase(application).audioDao()

    val AudioList : LiveData<List<Audio>> = audioDAO.getAudio()

    fun AddAudio(
        fileName: String,
        filePath: String
    ){
        viewModelScope.launch {
            audioDAO.saveAudio(
                Audio(
                    audio_Name = fileName,
                    audio_Path = filePath
                )
            )
        }
    }

    fun DeleteAudio(
        audio : Audio
    ){
        viewModelScope.launch {
            audioDAO.deleteAudio(audio)
        }
    }
}