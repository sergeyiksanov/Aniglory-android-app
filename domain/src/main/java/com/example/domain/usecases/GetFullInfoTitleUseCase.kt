package com.example.domain.usecases

import com.example.domain.repositories.ApiRepository

class GetFullInfoTitleUseCase(
    private val apiRepository: ApiRepository
) {

    suspend fun execute() = apiRepository.getFullInfoTitle()

}