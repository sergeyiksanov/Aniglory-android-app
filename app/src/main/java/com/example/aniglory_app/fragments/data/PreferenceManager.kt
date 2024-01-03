package com.example.aniglory_app.fragments.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.concurrent.Flow

class PreferenceManager(context: Context) {
    private val dataStore = context.createDataStore(name = "last_title_watching")

    companion object {
        val id_title = preferencesKey<String>("id_title")
        val progress = preferencesKey<Int>("progress")
    }

    suspend fun lastTitleTest(id: String, progress_: Int) {
        dataStore.edit { preferences ->
            preferences[id_title] = id
            preferences[progress] = progress_
        }
    }

//    val last_id: Flow<Int> = dataStore.data
//        .catch {
//            if (it is IOException) {
//                it.printStackTrace()
//                emit(emptyPreferences())
//            } else {
//                throw it
//            }
//        }
//        .map { preference ->
//            val id = preference[id_title]
//        }
}