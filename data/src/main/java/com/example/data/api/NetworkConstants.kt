package com.example.data.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson

object NetworkConstants {

    const val TOKEN = "8e349c0c9d4fbf42a2243c7e87b9ccd8"

    const val BASE_URL = "https://kodikapi.com"
    const val LIST_URL = "$BASE_URL/list"

    val httpClient = HttpClient(OkHttp){
        install(ContentNegotiation){
            gson()
        }
    }

}