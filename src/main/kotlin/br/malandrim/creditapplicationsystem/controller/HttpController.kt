package br.malandrim.creditapplicationsystem.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class HttpController{

    @GetMapping("/hello")
    fun helloWorld(): String{
        val hoje = LocalDateTime.now()

        return "Hello World !!! today is ${hoje.dayOfMonth}" +
                "/${hoje.monthValue}/${hoje.year}" +
                " - ${hoje.hour}:${hoje.minute}"
    }


}