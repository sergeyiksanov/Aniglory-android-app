package com.example.aniglory_app.fragments.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class ViewData: ViewModel() {
    val code_title: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}