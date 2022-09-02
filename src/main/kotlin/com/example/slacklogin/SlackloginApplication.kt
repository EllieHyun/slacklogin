package com.example.slacklogin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SlackloginApplication

fun main(args: Array<String>) {
	runApplication<SlackloginApplication>(*args)
}
