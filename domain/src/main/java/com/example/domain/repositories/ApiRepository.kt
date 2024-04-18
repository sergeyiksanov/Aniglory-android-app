package com.example.domain.repositories

import com.example.domain.models.FullInfoTitleModel
import com.example.domain.models.TitleModel

interface ApiRepository {

    suspend fun getListTitles(parameters: Map<String, String>): List<TitleModel>

    suspend fun getFullInfoTitle(): FullInfoTitleModel

    suspend fun getListGenres(): List<String>

    suspend fun getListYears(): List<String>

    suspend fun getListCountries(): List<String>

}