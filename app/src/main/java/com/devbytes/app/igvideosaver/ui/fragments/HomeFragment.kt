package com.devbytes.app.igvideosaver.ui.fragments

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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devbytes.app.igvideosaver.R
import com.devbytes.app.igvideosaver.databinding.FragmentHomeBinding
import com.devbytes.app.igvideosaver.ui.viewmodels.InstagramMediaViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: InstagramMediaViewModel by viewModels()
    private lateinit var downloadSnackBar: Snackbar
    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>
    private lateinit var clipboard: ClipboardManager

    companion object {
        private const val TAG = "HomeFragment"
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
            configurePermissions()

            requestMultiplePermissions.launch(
                arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
            )
        } else {
            val copyLink = binding.edCopyLink.text.toString()

            if (!TextUtils.isEmpty(copyLink)) {
                downloadSnackBar.show()
                viewModel.fetch(copyLink)
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

        requestMultiplePermissions =
            requireActivity().registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach {
                    Log.d(TAG, "${it.key} = ${it.value}")
                }
                if (permissions[READ_EXTERNAL_STORAGE] == true && permissions[WRITE_EXTERNAL_STORAGE] == true) {
                    Log.d(TAG, "Permission granted")
                    val copyLink = binding.edCopyLink.text.toString()

                    if (!TextUtils.isEmpty(copyLink)) {
                        downloadSnackBar.show()
                        viewModel.fetch(copyLink)
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