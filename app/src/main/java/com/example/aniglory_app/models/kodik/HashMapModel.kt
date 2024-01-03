package com.example.aniglory_app.models.kodik

data class HashMapModel(
    val list: HashMap<String, epModel>
)

data class epModel(
    val hls: hlsModel
)

data class hlsModel (
    val fhd: String,
    val hd: String,
    val sd: String
)
