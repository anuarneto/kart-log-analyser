package com.adaptti.kart.loganalyser.service

import com.adaptti.kart.loganalyser.model.Lap
import com.adaptti.kart.loganalyser.model.Racer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.InputStream
import java.time.Duration
import java.time.LocalTime

private val LAP_REGEX = "(\\d\\d:\\d\\d:\\d\\d.\\d{0,3})[\\s\\t]*(\\d\\d\\d)[\\s\\t][–\\-][\\s\\t]([A-Za-z0-9\\.]+)[\\s\\t]*(\\d+)[\\s\\t]*((((\\d?\\d):)?(\\d?\\d):)?(\\d?\\d)(\\.(\\d{0,3}))?)[\\s\\t]*([\\d\\.]?\\d+,\\d+)".toRegex()

fun InputStream.toLapSequence() =
        try {
            bufferedReader()
                    .readLines()
                    .asSequence()
                    .map(::parseLineToLap)
                    .filterNotNull()
        } catch (ex: IOException) {
            throw Exception("Could not read file. Please try again with another file!", ex)
        }

private fun parseLineToLap(line: String): Lap? {
    val match = LAP_REGEX.matchEntire(line) ?: return null
    return try {
        val time = LocalTime.parse(match.groupValues[1])
        val racer = Racer(match.groupValues[2].toInt())
        racer.name = match.groupValues[3]

        val lapNumber = match.groupValues[4].toInt()

        val lapHours = match.groupValues[8].toLongOrNull() ?: 0
        val lapMinutes = match.groupValues[9].toLongOrNull() ?: 0
        val lapSeconds = match.groupValues[10].toLongOrNull() ?: 0
        val strMillis = match.groupValues[12]
        val factor = when (strMillis.length) {
            0 -> 1
            1 -> 100
            2 -> 10
            3 -> 1
            else -> 0
        }
        val lapMillis = (strMillis.toLongOrNull() ?: 0) * factor
        val totalMillis = Duration
                .ofHours(lapHours)
                .plusMinutes(lapMinutes)
                .plusSeconds(lapSeconds)
                .plusMillis(lapMillis)
                .toMillis()
                .toInt()

        val speed = match.groupValues[13]
                .replace(".", "")
                .replace(',', '.')
                .toDouble()

        Lap(time, racer, lapNumber, totalMillis, speed)
    } catch (t: Throwable) {
        LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME).error("Fail to parse line: \"{}\"", line, t)
        null
    }
}
