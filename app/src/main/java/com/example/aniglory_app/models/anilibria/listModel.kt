package com.example.aniglory_app.models.anilibria

data class listModel(
    val id: Int,
    val code: String,
    val names: namesModel,
    val posters: postersModel
)

data class namesModel (
    val ru: String,
    val en: String,
    val alternative: String?
)

data class postersModel (
    val small: imageModel,
    val medium: imageModel,
    val original: imageModel
)
