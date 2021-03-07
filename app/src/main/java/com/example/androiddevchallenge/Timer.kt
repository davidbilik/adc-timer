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

import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.red
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Timer(
    value: Float
) {
    val tickColor = MaterialTheme.colors.onSurface
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .drawBehind {
                drawCircle(
                    center = center,
                    radius = 80f,
                    color = tickColor
                )
            }
    ) {
        val ticks = 60
        for (i in 0 until ticks) {
            val isBigTick = i % 5 == 0
            val angle = i * 360 / ticks
            TickMark(angle = angle, isBig = isBigTick)
            if (isBigTick) {
                TimerNumber(number = (60 - i - 15).correctionIfNecessary(), angle = angle)
            }
        }
        ProgressArc(progress = value / 60f)
        TimerFront()
    }
}

@Composable
fun TickMark(
    angle: Int,
    isBig: Boolean
) {
    val colors = MaterialTheme.colors
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                val radiusStartShorter = size.width / 2 * 0.6f
                val radiusStartBigger = size.width / 2 * 0.45f
                val radiusStart = if (isBig) radiusStartBigger else radiusStartShorter
                val radiusEnd = size.width / 2f * 0.7f
                val cos = cos(angle.toRad())
                val sin = sin(angle.toRad())
                val startOffset = Offset(cos * radiusStart, sin * radiusStart)
                val endOffset = Offset(cos * radiusEnd, sin * radiusEnd)
                drawLine(
                    color = colors.onSurface,
                    start = center + startOffset,
                    end = center + endOffset,
                    strokeWidth = if (isBig) 8f else 3f
                )
            }
    )
}

@Composable
fun TimerNumber(
    number: Int,
    angle: Int
) {
    val themeColors = MaterialTheme.colors
    val textSize = with(LocalDensity.current) { 30.sp.toPx() }
    val textPaint = remember {
        Paint().apply {
            color = themeColors.onSurface.toArgb()
            this.textSize = textSize
            textAlign = Paint.Align.CENTER
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                val cos = cos(angle.toRad())
                val sin = sin(angle.toRad())
                val radiusText = size.width / 2f * 0.85f
                val textOffset = Offset(cos * radiusText, sin * radiusText) + center
                val numberText = "$number"

                val textBounds = Rect()
                textPaint.getTextBounds(numberText, 0, numberText.length, textBounds)
                drawContext.canvas.nativeCanvas.drawText(
                    numberText,
                    textOffset.x,
                    textOffset.y + textBounds.height() / 2,
                    textPaint
                )
            }
    )
}

@Composable
fun ProgressArc(
    progress: Float
) {
    val animatedProgress by animateFloatAsState(targetValue = progress)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawArc(
                    red,
                    -90f,
                    -360 * animatedProgress,
                    topLeft = center - center * 0.7f,
                    size = size * 0.7f,
                    useCenter = true
                )
            }
    )
}

@Composable
fun TimerFront() {
    val circleColor = MaterialTheme.colors.background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawCircle(
                    center = center,
                    radius = 20f,
                    color = circleColor
                )
            }
    )
}

private fun Int.correctionIfNecessary(): Int {
    return if (this < 0) {
        60 + this
    } else {
        this
    }
}

private fun Int.toRad(): Float {
    return this * PI.toFloat() / 180
}
