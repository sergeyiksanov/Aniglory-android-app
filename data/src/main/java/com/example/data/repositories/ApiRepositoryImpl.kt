package com.example.data.repositories

import com.example.data.api.Api
import com.example.data.sources.remote.ApiRemoteSource
import com.example.domain.models.FullInfoTitleModel
import com.example.domain.models.TitleModel
import com.example.domain.repositories.ApiRepository

class ApiRepositoryImpl(
    private val apiRemoteSource: ApiRemoteSource
): ApiRepository {
    override suspend fun getListTitles(parameters: Map<String, String>): List<TitleModel> = apiRemoteSource.getListTitles(parameters)

    override suspend fun getFullInfoTitle(): FullInfoTitleModel {
        TODO("Not yet implemented")
    }

    override suspend fun getListGenres(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getListYears(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getListCountries(): List<String> {
        TODO("Not yet implemented")
    }
}