package com.example.domain.usecases

import com.example.domain.repositories.ApiRepository

class GetListGenresUseCase(
    private val apiRepository: ApiRepository
) {

    suspend fun execute() = apiRepository.getListGenres()

}