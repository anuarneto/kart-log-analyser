package com.adaptti.kart.loganalyser.service

import com.adaptti.kart.loganalyser.model.*
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.math.RoundingMode

@Service
class LogAnalyserService {

    fun analyse(file: MultipartFile) =
            try {
                file.inputStream
                        .toLapSequence()
                        .summarizeByRacer()
                        .calculateRaceResult()
            } catch (ex: IOException) {
                throw Exception("Could not read file. Please try again with another file!", ex)
            }

    private fun Sequence<Lap>.summarizeByRacer() = this
            .groupBy { it.racer }
            .map { (racer, laps) ->
                val totalLaps = laps.count()
                val totalMilliseconds = laps.sumBy { it.milliSeconds }
                val bestLap = laps.minBy { it.milliSeconds }
                val avgSpeed = laps.sumByDouble { it.avgSpeed }
                        .toBigDecimal()
                        .divide(totalLaps.toBigDecimal(), RoundingMode.HALF_EVEN)
                        .toDouble()
                Pair(
                        racer,
                        RacerSummary(
                                totalLaps,
                                totalMilliseconds,
                                bestLap,
                                avgSpeed)
                )
            }

    private fun List<Pair<Racer, RacerSummary>>.calculateRaceResult(): SummaryResponse {
        val sorted = sortedBy { it.second.totalMilliseconds }
        val firstRacer = sorted.first()
        val raceBestLap = sorted
                .minBy { it.second.bestLap?.milliSeconds ?: Int.MAX_VALUE }
                ?.second?.bestLap
        val positions = sorted.mapIndexed { index, pair ->
            RacerPosition(
                    pair.first,
                    index + 1,
                    firstRacer.second.totalMilliseconds - pair.second.totalMilliseconds,
                    pair.second
            )
        }
        return SummaryResponse(positions, raceBestLap)
    }
}