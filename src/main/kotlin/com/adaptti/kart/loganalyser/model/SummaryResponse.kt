package com.adaptti.kart.loganalyser.model

data class SummaryResponse(
        val positions: List<RacerPosition>,
        val bestLap: Lap?
)