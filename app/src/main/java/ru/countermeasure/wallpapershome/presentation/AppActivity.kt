package ru.countermeasure.wallpapershome.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.presentation.main.MainFragment
import ru.countermeasure.wallpapershome.utils.newRootScreen

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity)

        val isColdStart = savedInstanceState == null
        if (isColdStart)
            supportFragmentManager.newRootScreen(MainFragment::class.java)
    }
}