package com.devbytes.app.igvideosaver.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devbytes.app.igvideosaver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun init()
    {
//        TODO : Initialize components here
    }
}