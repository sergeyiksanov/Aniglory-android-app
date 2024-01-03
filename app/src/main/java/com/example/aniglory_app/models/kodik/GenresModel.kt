package com.example.aniglory_app.models.kodik

data class GenresModel (
    val results: List<result>
)

data class result (
    val title: String
)

data class YearsModel (
    val results: List<years>
)

data class years (
    val year: String
)