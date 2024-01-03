package com.example.aniglory_app.for_player

import android.util.Log

class JavaInterface {

    private var current_time: Int = 0
    private var full_time: Int = 1
    private var start_time: Int = 0

    fun setStartTime(new_start_time: Int) {
        start_time = new_start_time
    }

    fun getCurrTime() : Int = current_time

    @android.webkit.JavascriptInterface
    fun getStartTime() : Int {
        return start_time
    }

    @android.webkit.JavascriptInterface
    fun getCurrentTime(time: Int) {
        current_time = time
        Log.i("TEST_PROGRESS", current_time.toString() + " - current")
    }

    @android.webkit.JavascriptInterface
    fun getFullTime(time: Int) {
        full_time = time
        Log.i("TEST_PROGRESS", full_time.toString() + " - full")
    }

    fun getProgress() : Int {
        Log.i("TEST_PROGRESS", (current_time * 100 / full_time).toString() + "%")
        return (current_time * 100 / full_time)
    }

}