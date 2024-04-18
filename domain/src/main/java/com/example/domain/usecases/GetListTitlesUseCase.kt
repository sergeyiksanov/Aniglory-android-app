package com.example.domain.usecases

import com.example.domain.repositories.ApiRepository

class GetListTitlesUseCase(
    private val apiRepository: ApiRepository
) {

    suspend fun execute(parameters: Map<String, String>) = apiRepository.getListTitles(parameters)

}