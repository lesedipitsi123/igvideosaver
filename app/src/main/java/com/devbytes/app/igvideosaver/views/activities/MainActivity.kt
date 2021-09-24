package com.devbytes.app.igvideosaver.views.activities

import android.Manifest
import android.app.DownloadManager
import android.content.*
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkInfo
import com.devbytes.app.igvideosaver.R

import com.devbytes.app.igvideosaver.databinding.ActivityMainBinding
import com.devbytes.app.igvideosaver.utils.Constants
import com.devbytes.app.igvideosaver.utils.PermissionUtils
import com.devbytes.app.igvideosaver.viewmodels.DownloadViewModel
import com.google.android.material.snackbar.Snackbar
import android.text.TextUtils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: DownloadViewModel by lazy { ViewModelProvider(this).get(DownloadViewModel::class.java) }
    private val permissionUtils: PermissionUtils = PermissionUtils(this)
    private lateinit var downloadSnackbar: Snackbar
    private lateinit var clipboard : ClipboardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        configure()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.READ_WRITE_CODE) {
            if (grantResults.isEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this,
                    "Please enable permissions to allow for videos to be saved.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val copyLink = binding.edCopyLink.text.toString()

                if (!TextUtils.isEmpty(copyLink)) {
                    downloadSnackbar.show()
                    viewModel.downloadVideo(copyLink)
                } else
                    Snackbar.make(
                        binding.root,
                        getString(R.string.err_empty_input),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
            }
        }
    }

    private fun configure() {
        clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        with(binding) {
            downloadSnackbar = Snackbar.make(
                root,
                this@MainActivity.getString(R.string.notification_download_in_progress),
                Snackbar.LENGTH_LONG
            )

            btnDownload.setOnClickListener {

                permissionUtils.requestPermission(
                    Constants.READ_WRITE_CODE,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }

            btnPaste.setOnClickListener {
                if (clipboard.hasPrimaryClip()) {
                    clipboard.primaryClipDescription?.let { clipDescription ->
                        if (clipDescription.hasMimeType(MIMETYPE_TEXT_PLAIN)) {
                            clipboard.primaryClip?.let { it ->
                                val item = it.getItemAt(0).text

                                if (item.contains(Regex("copy_link"))){
                                    binding.edCopyLink.setText(item)
                                }
                                else {
                                    Toast.makeText(this@MainActivity, "Invalid link copied.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }

        // viewModel.outputWorkInfo.observe(this, workInfoObserver())

        registerReceiver(
            onDownloadComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    private fun workInfoObserver(): Observer<List<WorkInfo>> {
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
                downloadSnackbar.setText(this@MainActivity.getString(R.string.notification_download_finished))
                downloadSnackbar.show()

                // Normally this processing, which is not directly related to drawing views on
                // screen would be in the ViewModel. For simplicity we are keeping it here.

                // If there is an output file show "See File" button

            } else {
                downloadSnackbar.show()
            }
        }
    }

    private var onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            downloadSnackbar.setText(this@MainActivity.getString(R.string.notification_download_finished))
            downloadSnackbar.show()
        }
    }
}