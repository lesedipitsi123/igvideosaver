package com.devbytes.app.igvideosaver.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.devbytes.app.igvideosaver.R
import com.devbytes.app.igvideosaver.databinding.FragmentHomeBinding
import com.devbytes.app.igvideosaver.databinding.FragmentSavedVideosBinding

class SavedVideos : Fragment() {

    private var _binding: FragmentSavedVideosBinding? = null
    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance() =
            SavedVideos()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedVideosBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configure()
    }

    private fun configure() {
        binding.toolbar.navigationIcon = AppCompatResources.getDrawable(
            requireContext(),
            R.mipmap.outline_arrow_back_black_24
        )?.apply {
            setTint(ResourcesCompat.getColor(resources, R.color.colorText, null))
        }

        binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
    }
}