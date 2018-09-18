package com.adaptti.kart.loganalyser

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class KartLogAnalyserApplication

fun main(args: Array<String>) {
    SpringApplication.run(KartLogAnalyserApplication::class.java, *args)
}