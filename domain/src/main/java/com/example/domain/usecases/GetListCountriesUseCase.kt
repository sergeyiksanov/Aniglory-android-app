package com.example.domain.usecases

import com.example.domain.repositories.ApiRepository

class GetListCountriesUseCase(
    private val apiRepository: ApiRepository
) {

    suspend fun execute() = apiRepository.getListCountries()

}