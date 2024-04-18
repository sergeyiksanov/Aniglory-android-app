package com.example.data.api

import android.util.Log
import com.example.domain.models.TitleModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType

class Api {

    private var urlList = NetworkConstants.LIST_URL

    suspend fun getListTitles(parameters: Map<String, String>): List<TitleModel> {

        try {
            val response = NetworkConstants.httpClient.request {
                url(urlList)
                parameter("token", NetworkConstants.TOKEN)
                parameters.forEach {
                    parameter(it.key, it.value)
                }
                method = HttpMethod.Get
                contentType(ContentType.Application.Json)
            }

            if(response.status.value in 200..299) {
                val data: JsonElement = response.body()
                val titles: List<TitleModel> = listOf(Gson().fromJson(data.asJsonObject.get("results"), TitleModel::class.java))

                return titles
            }
        } catch (e: Exception) {
            Log.e("GET_LIST_TITLES", e.toString())
        }

        return emptyList()
    }

}