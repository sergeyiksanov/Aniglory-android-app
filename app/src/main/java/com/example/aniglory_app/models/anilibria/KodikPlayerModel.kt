package com.example.aniglory_app.models.anilibria

import com.google.gson.JsonElement

data class KodikPlayerModel(
    val results: List<resModel>
)

data class resModel (
    val seasons: JsonElement
)
