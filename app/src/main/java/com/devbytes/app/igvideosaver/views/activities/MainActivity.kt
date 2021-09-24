package com.devbytes.app.igvideosaver.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.devbytes.app.igvideosaver.R

import com.devbytes.app.igvideosaver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        configure()
    }

    private fun configure() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_home_host) as NavHostFragment
        val navController = navHostFragment.navController
    }
}