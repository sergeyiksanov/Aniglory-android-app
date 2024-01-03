package com.example.aniglory_app.models.anilibria

import com.example.aniglory_app.models.anilibria.listModel

data class TitlesModel(
    val list: MutableList<listModel>
)

//data class listModel(
//    val id: Int,
//    val code: String,
//    val names: namesModel,
//    val posters: postersModel,
//    val type: typeModel
//)

//data class namesModel(
//    val ru: String,
//    val en: String
//)
//
//data class postersModel(
//    val small: posterSmall,
//    val medium: posterMedium,
//    val original: posterOriginal
//)
//
//data class posterSmall(
//    val url: String
//)
//
//data class posterMedium(
//    val url: String
//)
//
//data class posterOriginal(
//    val url: String
//)
//
//data class typeModel(
//    val full_string: String
//)
