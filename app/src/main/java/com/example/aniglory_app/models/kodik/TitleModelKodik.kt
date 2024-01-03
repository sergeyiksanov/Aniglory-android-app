package com.example.aniglory_app.models.kodik

data class TitleModelKodik(
    val id: String,
    val title: String,
    val title_orig: String,
    val year: String,
    val material_data: MaterialModel,
    val seasons: HashMap<String, seasonModel>
)

data class MaterialModel(
    val description: String,
    val poster_url: String
)