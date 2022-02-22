package com.nicity.nicitytestproject.presentation.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nicity.nicitytestproject.R
import com.nicity.nicitytestproject.utils.extensions.navigateToFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.navigateToFragment(fragment = NavigationFragment())
    }
}