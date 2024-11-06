package com.api.pulley

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class PulleyApplication

fun main(args: Array<String>) {
    runApplication<PulleyApplication>(*args)
}
