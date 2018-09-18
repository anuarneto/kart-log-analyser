package com.adaptti.kart.loganalyser.model

import java.time.LocalTime

data class Lap(
        val time: LocalTime,
        val racer: Racer,
        val number: Int,
        val milliSeconds: Int,
        val avgSpeed: Double
)