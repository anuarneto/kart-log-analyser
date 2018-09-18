package com.adaptti.kart.loganalyser.model

data class RacerPosition(
        val racer: Racer,
        val position: Int,
        val millisToFirst: Int,
        val summary: RacerSummary
)