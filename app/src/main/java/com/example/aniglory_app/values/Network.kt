package com.example.aniglory_app.values

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson

object Network {
    val API_TOKEN = "8e349c0c9d4fbf42a2243c7e87b9ccd8"
    val BASE_URL = "https://api.anilibria.tv/v3"
    val TITLES_URL = "$BASE_URL/title/updates"
    val TITLE_URL = "$BASE_URL/title"
    val SEARCH_URL = "$BASE_URL/title/search"
    val KODIK_PLAYER = "https://kodikapi.com/search"
    var KODIK_LIST = "https://kodikapi.com/list"
    val IMAGE_HOST = "https://aaa.anilibria.sbs"
    val PLAYER_ANILIBRIA_HOST = "https://cache.libria.fun"
    val GENRES_KODIK = "https://kodikapi.com/genres"
    val KODIK_COUNTRIES = "https://kodikapi.com/countries"
    val KODIK_YEARS = "https://kodikapi.com/years"

    val TELEGRAMM_HELP = "https://t.me/sergeyiksanov"

    val httpClient = HttpClient(OkHttp){
        install(ContentNegotiation){
            gson()
        }
    }
}