package com.example.slacklogin

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/slack-login")
class SlackLoginController @Autowired constructor(
    private val autoService: AutoService
) {
    @GetMapping
    fun login(@RequestParam code: String) {

        // TODO code를 받아오는 로직

        autoService.login(code)
    }
}