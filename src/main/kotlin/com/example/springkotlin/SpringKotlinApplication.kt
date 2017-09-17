package com.example.springkotlin

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SpringKotlinApplication

fun main(args: Array<String>) {
    ObjectMapper().registerModule(KotlinModule())
    SpringApplication.run(SpringKotlinApplication::class.java, *args)
}
