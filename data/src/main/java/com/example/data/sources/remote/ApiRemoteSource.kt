package com.example.data.sources.remote

import com.example.data.api.Api

class ApiRemoteSource(
    private val api: Api
) {

    suspend fun getListTitles(parameters: Map<String, String>) = api.getListTitles(parameters)

}