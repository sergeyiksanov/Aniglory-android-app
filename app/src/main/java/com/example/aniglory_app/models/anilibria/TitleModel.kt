package com.example.aniglory_app.models.anilibria

data class TitleModel (
    val code: String,
    val names: namesModel,
    val status: statusModel,
    val posters: postersModel,
    val type: typeModel,
    val genres: List<String>,
    val description: String,
    val season: seasonModel,
    val player: playerModel
)

data class playerModel (
    val host: String,
    val episodes: episodesModel
)

data class episodesModel (
    val last: Int
)

data class statusModel (
    val string: String,
    val code: Int
)

data class typeModel (
    val full_string: String,
    val episodes: Int?
)

data class seasonModel (
    val string: String,
    val year: Int
)
