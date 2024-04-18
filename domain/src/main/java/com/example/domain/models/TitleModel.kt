package com.example.domain.models

data class TitleModel(
    val id: String,
    val title: String,
    val title_orig: String,
    val year: String,
    val material_data: MaterialModel
)

data class MaterialModel(
    val description: String,
    val poster_url: String
)