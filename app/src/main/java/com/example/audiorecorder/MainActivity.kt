package com.example.audiorecorder

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import android.Manifest
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.audiorecorder.Recorder.AndroidAudioRecorder
import com.example.audiorecorder.RoomSetup.Audio
import com.example.audiorecorder.RoomSetup.AudioDatabase
import com.example.audiorecorder.RoomSetup.AudioViewModel
import com.example.audiorecorder.screen.main_scr
import com.example.audiorecorder.ui.theme.AudioRecorderTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var audioViewModel: AudioViewModel
    private lateinit var audioRecorder: AndroidAudioRecorder

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )

        setContent {
            AudioRecorderTheme {

                audioViewModel = ViewModelProvider(this)[AudioViewModel::class.java]
                audioRecorder = AndroidAudioRecorder(this)

                main_scr(audioRecorder, audioViewModel, this)
            }
        }
    }
}

@Composable
fun Display_App() {

}