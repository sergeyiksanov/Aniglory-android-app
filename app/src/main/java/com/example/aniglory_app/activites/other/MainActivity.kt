package com.example.aniglory_app.activites.other

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.aniglory_app.R
import com.example.aniglory_app.fragments.footer.MenuFragment
import com.example.aniglory_app.fragments.header.SearchFragment
import com.example.aniglory_app.fragments.body.TitlesFragment
import com.example.aniglory_app.fragments.body.new_interface.HomeScreenFragment
import com.example.aniglory_app.fragments.footer.new_interface.NavigationMenuFragment
import com.example.data.api.Api
import com.example.data.repositories.ApiRepositoryImpl
import com.example.data.sources.remote.ApiRemoteSource
import com.example.domain.usecases.GetListTitlesUseCase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        lifecycleScope.launch {
//            Log.i("TEST_ARCH", GetListTitlesUseCase(ApiRepositoryImpl(ApiRemoteSource(Api()))).execute(mapOf()).toString())
//        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.headerFragment, SearchFragment.newInstance()).commit()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.bodyFragment, HomeScreenFragment.newInstance()).commit()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.footerFragment, NavigationMenuFragment.newInstance()).commit()
    }

}