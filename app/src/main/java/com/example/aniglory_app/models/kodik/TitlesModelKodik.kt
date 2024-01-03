package com.example.aniglory_app.models.kodik

import com.google.gson.JsonElement

data class TitlesModelKodik(
    val results: MutableList<resultsModel>
)

data class resultsModel (
    val title: String?,
    val title_orig: String?,
    val material_data: matiralModel?,
    val id: String?,
    val year: String?,
    val seasons: HashMap<String, seasonModel>?,
    val episodes_count: String
)

data class matiralModel(
    val poster_url: String?,
    val description: String?
)

data class seasonModel (
    val episodes: HashMap<String, String>?
)