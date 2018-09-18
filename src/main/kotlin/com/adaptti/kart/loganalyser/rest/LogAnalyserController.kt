package com.adaptti.kart.loganalyser.rest

import com.adaptti.kart.loganalyser.model.SummaryResponse
import com.adaptti.kart.loganalyser.service.LogAnalyserService
import io.swagger.annotations.Api
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.time.Duration

/**
 * Serviços Rest para controlar modelos treinados (Adicionar/Remover/Listar/Utilizar)
 */
@Suppress("unused")
@RestController()
@RequestMapping(path = ["/kart/log"])
@Api(value = "Analisador de logs de corrida de Kart",
        description = "Serviço para analisar log de corrida de Kart",
        basePath = "/kart/log", tags = ["Analisador logs Kart"])
internal class LogAnalyserController(val logAnalyserService: LogAnalyserService) {

    private val headers = listOf(
            "Posição Chegada",
            "Código Piloto",
            "Nome Piloto",
            "Qtde Voltas Completadas",
            "Tempo Total de Prova"
    )
    private val headersDetails = listOf(
            "Posição Chegada",
            "Código Piloto",
            "Nome Piloto",
            "Qtde Voltas Completadas",
            "Tempo Total de Prova",
            "Melhor volta",
            "Velocidade média",
            "Diferênça para o 1o"
    )

    @PostMapping(path = ["analyse/json"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun analyseJson(@RequestParam("file") file: MultipartFile?): ResponseEntity<SummaryResponse> {
        if (file == null) {
            return ResponseEntity.noContent().build()
        }
        val summary = logAnalyserService.analyse(file)
        return ResponseEntity.ok(summary)
    }

    @PostMapping(path = ["analyse"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun analyse(@RequestParam("file") file: MultipartFile?): ResponseEntity<String> {
        if (file == null) {
            return ResponseEntity.noContent().build()
        }
        val summary = logAnalyserService.analyse(file)
        val separator = "; "
        val html = StringBuilder()
        html.appendln(headers.joinToString(separator))
        summary.positions.forEach {
            html
                    .append(it.position)
                    .append(separator).append(it.racer.id)
                    .append(separator).append(it.racer.name)
                    .append(separator).append(it.summary.totalLaps)
                    .append(separator).append(it.summary.totalMilliseconds.prettyDuration())
                    .appendln()
        }

        return ResponseEntity.ok(html.toString())
    }

    @PostMapping(path = ["analyse/details"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun analyseDetails(@RequestParam("file") file: MultipartFile?): ResponseEntity<String> {
        if (file == null) {
            return ResponseEntity.noContent().build()
        }
        val summary = logAnalyserService.analyse(file)
        val separator = "; "

        val html = StringBuilder()
        html.appendln(headersDetails.joinToString(separator))
        summary.positions.forEach {
            html
                    .append(it.position)
                    .append(separator).append(it.racer.id)
                    .append(separator).append(it.racer.name)
                    .append(separator).append(it.summary.totalLaps)
                    .append(separator).append(it.summary.totalMilliseconds.prettyDuration())
                    .append(separator)
            if (it.summary.bestLap != null) {
                html.append(it.summary.bestLap.number)
            } else {
                html.append("-")
            }
            html.append(separator).append(it.summary.averageSpeed.toString().replace(",", "").replace('.', ','))
            html.append(separator).append(it.millisToFirst.prettyDuration())
            html.appendln()
        }

        html.appendln()
                .appendln("Melhor volta da corrida:")
        if (summary.bestLap != null) {
            html.append(summary.bestLap.racer.id).append(" - ").append(summary.bestLap.racer.name).append(separator)
                    .append(" em ").append(summary.bestLap.milliSeconds.prettyDuration())
                    .append(" às ").append(summary.bestLap.time)
        }
        return ResponseEntity.ok(html.toString())
    }

    private val durationPrintRegex = "(\\d[HMS])(?!$)".toRegex()

    private fun Int.prettyDuration() =
            Duration.ofMillis(toLong()).toString()
                    .substring(2)
                    .replace(durationPrintRegex, "$1 ")
                    .toLowerCase()
                    .replace('.', ',')

}
