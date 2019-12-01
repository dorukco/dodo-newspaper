package com.dodo.newspaper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NewspaperApplication

fun main(args: Array<String>) {
	runApplication<NewspaperApplication>(*args)
}
