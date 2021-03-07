/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Compdown") })
        },
        content = {
            Surface(color = MaterialTheme.colors.surface) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    var timerValue by remember { mutableStateOf(0f) }
                    var timer by remember { mutableStateOf<Timer?>(null) }

                    fun cancelTimer() {
                        timer?.cancel()
                        timer = null
                    }

                    Timer(timerValue)

                    Spacer(modifier = Modifier.height(80.dp))

                    TimerSettings(timerValue.toInt(), onValueChange = { timerValue = it.toFloat() })

                    Spacer(modifier = Modifier.height(32.dp))

                    TimerControl(
                        timerRunning = timer != null,
                        onStopCountdown = {
                            cancelTimer()
                        },
                        onStartCountdown = {
                            timer = fixedRateTimer(
                                period = 250
                            ) {
                                timerValue = (timerValue - 0.25f).coerceAtLeast(0f)
                                if (timerValue <= 0f) {
                                    cancelTimer()
                                }
                            }
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun TimerSettings(
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Button(
            modifier = Modifier.size(44.dp),
            onClick = {
                onValueChange((value - 5).coerceAtLeast(0))
            }
        ) {
            ControlButtonText("-")
        }

        Text(
            fontSize = 44.sp,
            text = "$value"
        )

        Button(
            modifier = Modifier.size(44.dp),
            onClick = {
                onValueChange((value + 5).coerceAtMost(60))
            }
        ) {
            ControlButtonText(text = "+")
        }
    }
}

@Composable
private fun ControlButtonText(text: String) {
    Text(text = text, color = Color.White, fontSize = 20.sp)
}

@Composable
private fun TimerControl(
    timerRunning: Boolean,
    onStopCountdown: () -> Unit,
    onStartCountdown: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(
            onClick = {
                if (!timerRunning) {
                    onStartCountdown()
                } else {
                    onStopCountdown()
                }
            }
        ) {
            Crossfade(targetState = timerRunning) {
                Icon(
                    if (!it) Icons.Default.PlayArrow else Icons.Default.Stop,
                    modifier = Modifier.size(48.dp),
                    contentDescription = "Start/Stop button"
                )
            }
        }
    }
}

@Preview("App", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}
