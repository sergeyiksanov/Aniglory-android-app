package com.example.aniglory_app.activites.other

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.aniglory_app.R
import com.example.aniglory_app.fragments.footer.MenuFragment
import com.example.aniglory_app.fragments.header.SearchFragment
import com.example.aniglory_app.fragments.body.TitlesFragment
import com.example.aniglory_app.fragments.body.new_interface.HomeScreenFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.headerFragment, SearchFragment.newInstance()).commit()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.bodyFragment, HomeScreenFragment.newInstance()).commit()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.footerFragment, MenuFragment.newInstance()).commit()
    }

}