package com.devbytes.app.igvideosaver.views.fragments

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.work.WorkInfo
import com.devbytes.app.igvideosaver.R
import com.devbytes.app.igvideosaver.databinding.FragmentHomeBinding
import com.devbytes.app.igvideosaver.utils.Constants
import com.devbytes.app.igvideosaver.utils.PermissionUtils
import com.devbytes.app.igvideosaver.viewmodels.DownloadViewModel
import com.google.android.material.snackbar.Snackbar

class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DownloadViewModel by lazy { ViewModelProvider(this).get(DownloadViewModel::class.java) }
    private lateinit var permissionUtils: PermissionUtils
    private lateinit var downloadSnackBar: Snackbar
    private lateinit var requestMultiplePermissions : ActivityResultLauncher<Array<String>>
    private lateinit var clipboard: ClipboardManager

    companion object {
        private const val TAG = "HomeFragment"

        @JvmStatic
        fun newInstance() =
            Home()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configure()
    }

    private fun configure() {
        permissionUtils = PermissionUtils(requireActivity())
        configurePermissions()

        clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        with(binding) {
            downloadSnackBar = Snackbar.make(
                root,
                requireContext().getString(R.string.notification_download_in_progress),
                Snackbar.LENGTH_LONG
            )

            btnView.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_history)
            }

            btnDownload.setOnClickListener {
                checkPermissions()
            }

            btnPaste.setOnClickListener {
                if (clipboard.hasPrimaryClip()) {
                    clipboard.primaryClipDescription?.let { clipDescription ->
                        if (clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            clipboard.primaryClip?.let { it ->
                                val item = it.getItemAt(0).text

                                if (item.contains(Regex("copy_link"))) {
                                    binding.edCopyLink.setText(item)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Invalid link copied.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }

        // viewModel.outputWorkInfo.observe(this, workInfoObserver())

        requireActivity().registerReceiver(
            onDownloadComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "Request Permissions")
            requestMultiplePermissions.launch(
                arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
            )
        } else {
            val copyLink = binding.edCopyLink.text.toString()

            if (!TextUtils.isEmpty(copyLink)) {
                downloadSnackBar.show()
                viewModel.downloadVideo(copyLink)
            } else
                Snackbar.make(
                    binding.root,
                    getString(R.string.err_empty_input),
                    Snackbar.LENGTH_SHORT
                )
                    .show()

            Log.d(TAG, "Permission Already Granted")
        }
    }

    private fun configurePermissions() {
        requestMultiplePermissions = requireActivity().registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d(TAG, "${it.key} = ${it.value}")
            }
            if (permissions[READ_EXTERNAL_STORAGE] == true && permissions[WRITE_EXTERNAL_STORAGE] == true) {
                Log.d(TAG, "Permission granted")
                val copyLink = binding.edCopyLink.text.toString()

                if (!TextUtils.isEmpty(copyLink)) {
                    downloadSnackBar.show()
                    viewModel.downloadVideo(copyLink)
                } else
                    Snackbar.make(
                        binding.root,
                        getString(R.string.err_empty_input),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
            } else {
                Log.d(TAG, "Permission not granted")
            }
        }
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
                downloadSnackBar.setText(requireContext().getString(R.string.notification_download_finished))
                downloadSnackBar.show()

                // Normally this processing, which is not directly related to drawing views on
                // screen would be in the ViewModel. For simplicity we are keeping it here.

                // If there is an output file show "See File" button

            } else {
                downloadSnackBar.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(onDownloadComplete)
    }

    private var onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            downloadSnackBar.setText(requireActivity().getString(R.string.notification_download_finished))
            downloadSnackBar.show()
        }
    }
}