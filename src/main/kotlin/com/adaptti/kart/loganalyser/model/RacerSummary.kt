package com.adaptti.kart.loganalyser.model

data class RacerSummary(
        val totalLaps: Int,
        val totalMilliseconds: Int,
        val bestLap: Lap?,
        val averageSpeed: Double
)