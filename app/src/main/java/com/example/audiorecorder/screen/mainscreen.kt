package com.example.audiorecorder.screen

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.trace
import androidx.compose.ui.zIndex
import com.example.audiorecorder.R
import com.example.audiorecorder.Recorder.AndroidAudioRecorder
import com.example.audiorecorder.Recorder.AudioRecorder
import com.example.audiorecorder.Recorder.Timer
import com.example.audiorecorder.RoomSetup.Audio
import com.example.audiorecorder.RoomSetup.AudioViewModel
import com.example.audiorecorder.ui.theme.background
import com.example.audiorecorder.ui.theme.foreground
import com.example.audiorecorder.ui.theme.list_bg
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun main_scr(
    audioRecorder: AndroidAudioRecorder,
    audioViewModel: AudioViewModel,
    context: Context
) {

    var isRecording by remember { mutableStateOf(false) }
    var currentRecordingFile by remember { mutableStateOf<File?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    // Create media player for playback
    val mediaPlayer = remember {
        MediaPlayer().apply {
            setOnCompletionListener {
                isPlaying = false
            }
        }
    }

    val audios by audioViewModel.AudioList.observeAsState(initial = emptyList())

    var timerText by remember { mutableStateOf("00:00.00") }


    val timer = Timer(object : Timer.OnTimerTickListener {
        override fun onTimerTick(duration: String) {
            timerText = duration
            Log.d("Timer", "Time: $duration")
        }
    })

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors =
                topAppBarColors(
                    containerColor = foreground,
                    titleContentColor = Color.White
                ),
                title = { Text("Audio Recorder") }
            )
        }) { padding ->

        //main box for whole screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp, 10.dp, 20.dp, 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //for above part
                Box(
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Card(
                        colors = CardDefaults.cardColors(foreground),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
//                            .height(200.dp)
                            .padding(0.dp, 0.dp, 0.dp, 40.dp)

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.mic),
                                contentDescription = if (isRecording) "Recording" else "Not Recording",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(background)
                                    .border(
                                        10.dp,
                                        if (isRecording) Color.Red else list_bg,
                                        CircleShape
                                    )
                                    .padding(20.dp),
                                colorFilter = ColorFilter.tint(foreground)
                            )

                            //space for visualizer
//                            Box(
//                                modifier = Modifier
//                                    .height(150.dp)
//                                    .width(20.dp)
//                                    .padding(0.dp, 20.dp, 0.dp, 40.dp)
//                                    .background(Color.White)
//
//                            )
                            Text(text = timerText, fontSize = 50.sp, color = Color.White, modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 40.dp))
                        }
                    }

                    IconButton(
                        onClick = {
                            if (isRecording) {
                                // Stop recording
                                timer.stop()
                                audioRecorder.stop()
                                audioViewModel.AddAudio(currentRecordingFile!!.name, currentRecordingFile!!.absolutePath)
                                isRecording = false
                            } else {
                                // Start recording with a unique filename
                                timer.start()
                                val fileName = "recording_${System.currentTimeMillis()}.mp3"
                                val file = File(context.filesDir, fileName)
                                currentRecordingFile = file
                                audioRecorder.start(file)
                                isRecording = true
                            }

                            Log.d("checking_Variable", currentRecordingFile.toString())
                        },
                        modifier = Modifier.size(70.dp)
                    ) {
                        Icon(
                            imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.PlayCircleOutline,
                            contentDescription = if (isRecording) "Stop Recording" else "Start Recording",
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color.White)
                                .padding(2.dp),
                            tint = if (isRecording) Color.Red else foreground
                        )
                    }
                }


                //Lower part for display recordings
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 8.dp, 0.dp, 0.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {//list of Recorded Audios

//                    IconButton(onClick = {
////                        mediaPlayer.setDataSource(audioFiles?.absolutePath)
////                        mediaPlayer.prepare()
////                        mediaPlayer.start()
//                    }) { Text("Play") }



//                    IconButton(
//                        onClick = {
//                            currentRecordingFile?.let { file ->
//                                if (file.exists()) {
//                                    if (isPlaying) {
//                                        mediaPlayer.stop()
//                                        mediaPlayer.reset()
//                                        isPlaying = false
//                                    } else {
//                                        try {
//                                            mediaPlayer.reset()
//                                            mediaPlayer.setDataSource(file.absolutePath)
//                                            mediaPlayer.prepare()
//                                            mediaPlayer.start()
//                                            isPlaying = true
//                                        } catch (e: Exception) {
//                                            Log.e("AudioPlayer", "Error playing audio", e)
//                                        }
//                                    }
//                                }
//                            }
//                        },
//                        enabled = currentRecordingFile != null
//                    ) {
//                        Text(if (isPlaying) "Stop" else "Play")
//                    }



                    LazyColumn() {
                        items(audios){
                            audio ->
                            player_scr(
                                recording_data = audio,
                                viewModel = audioViewModel
                            )
                        }
                    }



                }
            }
        }
    }
}



//@Preview(showSystemUi = true, device = "id:pixel_9_pro")
//@Composable
//private fun ui() {
//    main_scr()
//}