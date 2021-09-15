package com.devbytes.app.igvideosaver.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkInfo

import com.devbytes.app.igvideosaver.databinding.ActivityMainBinding
import com.devbytes.app.igvideosaver.viewmodels.DownloadViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: DownloadViewModel by lazy { ViewModelProvider(this).get(DownloadViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.btnDownload.setOnClickListener {
            viewModel.downloadVideo()
        }

        viewModel.outputWorkInfo.observe(this, workInfosObserver())
    }

    private fun workInfosObserver(): Observer<List<WorkInfo>> {
        return Observer { listOfWorkInfo ->

            // Note that these next few lines grab a single WorkInfo if it exists
            // This code could be in a Transformation in the ViewModel; they are included here
            // so that the entire process of displaying a WorkInfo is in one location.

            // If there are no matching work info, do nothing
            if (listOfWorkInfo.isNullOrEmpty()) {
                return@Observer
            }

            // We only care about the one output status.
            // Every continuation has only one worker tagged TAG_OUTPUT
            val workInfo = listOfWorkInfo[0]

            if (workInfo.state.isFinished) {
                showWorkFinished()

                // Normally this processing, which is not directly related to drawing views on
                // screen would be in the ViewModel. For simplicity we are keeping it here.

                // If there is an output file show "See File" button

            } else {
                showWorkInProgress()
            }
        }
    }

    /**
     * Shows and hides views for when the Activity is processing an image
     */
    private fun showWorkInProgress() {
        with(binding) {
            Toast.makeText(this@MainActivity, "Downloading file", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Shows and hides views for when the Activity is done processing an image
     */
    private fun showWorkFinished() {
        with(binding) {
            Toast.makeText(this@MainActivity, "Download finished", Toast.LENGTH_SHORT).show()
        }
    }
}