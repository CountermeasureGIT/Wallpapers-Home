package ru.countermeasure.wallpapershome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.countermeasure.wallpapershome.ui.main.MainFragment
import ru.countermeasure.wallpapershome.utils.newRootScreen

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity)

        val isColdStart = savedInstanceState == null
        if (isColdStart)
            supportFragmentManager.newRootScreen(MainFragment::class.java)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}