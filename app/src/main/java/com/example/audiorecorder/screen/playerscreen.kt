package com.example.audiorecorder.screen

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.audiorecorder.RoomSetup.Audio
import com.example.audiorecorder.RoomSetup.AudioViewModel
import com.example.audiorecorder.ui.theme.background
import com.example.audiorecorder.ui.theme.foreground
import kotlinx.coroutines.delay

@Composable
fun player_scr(recording_data: Audio, viewModel: AudioViewModel) {

    var isPlaying by rememberSaveable { mutableStateOf(false) }

    var _expanded by rememberSaveable { mutableStateOf(false) }

    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var duration by remember{ mutableStateOf(0) }

println(sliderPosition)
    // Create media player for playback
    val mediaPlayer = remember {
        MediaPlayer().apply {
            setOnCompletionListener {
                isPlaying = false
                sliderPosition = 0f
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            sliderPosition = mediaPlayer.currentPosition.toFloat()
            delay(100L) // Update every 100ms (smooth feel)
        }
    }


    Card(
        colors = CardDefaults.cardColors(foreground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp), contentAlignment = Alignment.Center) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    isPlaying = !isPlaying

                    if (isPlaying) {
                        try {
                            mediaPlayer.reset()
                            mediaPlayer.setDataSource(recording_data.audio_Path)
                            mediaPlayer.prepare()
                            mediaPlayer.start()

                            duration = mediaPlayer.duration

                        } catch (e: Exception) {
                            e.printStackTrace()
                            isPlaying = false
                        }
                    } else {
                        mediaPlayer.pause()
                    }

                }) {
                    Icon(
                        if (isPlaying) Icons.Default.PauseCircleOutline else Icons.Default.PlayCircleOutline,
                        contentDescription = if (isPlaying) "Playing" else "Not Playing",
                        tint = if (isPlaying) Color.White else background,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Column(modifier = Modifier
                    .fillMaxWidth(.8f)
                    .padding(4.dp, 4.dp, 0.dp, 0.dp)) {

                    Text(
                        text = recording_data.audio_Name,
                        maxLines = 1,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.padding(10.dp, 2.dp, 0.dp, 0.dp)
                    )

                    Slider(
                        value = sliderPosition,
                        onValueChange = { sliderPosition = it
                                        mediaPlayer.seekTo(it.toInt())},
                        colors = SliderDefaults.colors(
                            thumbColor = if (isPlaying) Color.White else background,
                            activeTrackColor = background,
                            inactiveTrackColor = Color(0xFF534582),
                        ),
                        valueRange = 0f..duration.toFloat()
                    )
                }

                IconButton(onClick = { _expanded = !_expanded }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Menu for Rename and Delete", tint = Color.White
                    )
                }
                DropdownMenu(
                    expanded = _expanded,
                    onDismissRequest = { _expanded = false },
                    modifier = Modifier
                        .width(100.dp),
                    offset = DpOffset(x = 150.dp, y = -30.dp),

                    ) {
                    DropdownMenuItem(text = { Text(text = "Delete") },
                        onClick = {viewModel.DeleteAudio(recording_data)})
                }
            }
        }

    }
}


@Preview()
@Composable
private fun ui() {
//    player_scr()
}