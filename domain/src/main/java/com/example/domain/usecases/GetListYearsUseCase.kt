package com.example.domain.usecases

import com.example.domain.repositories.ApiRepository

class GetListYearsUseCase(
    private val apiRepository: ApiRepository
) {

    suspend fun execute() = apiRepository.getListYears()

}