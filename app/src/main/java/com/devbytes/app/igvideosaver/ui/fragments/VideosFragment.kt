package com.devbytes.app.igvideosaver.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.devbytes.app.igvideosaver.R
import com.devbytes.app.igvideosaver.databinding.FragmentVideosBinding
import com.devbytes.app.igvideosaver.ui.fragments.adapters.VidoesListAdapter
import com.devbytes.app.igvideosaver.ui.viewmodels.InstagramMediaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideosFragment : Fragment() {
    private var _binding: FragmentVideosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: InstagramMediaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideosBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configure()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    private fun configure() {
        val adapter = VidoesListAdapter(requireContext())
        viewModel.getVideos()

        with(binding) {
            toolbar.navigationIcon = AppCompatResources.getDrawable(
                requireContext(),
                R.mipmap.outline_arrow_back_black_24
            )?.apply {
                setTint(ResourcesCompat.getColor(resources, R.color.colorText, null))
            }

            toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

            recyclerview.apply {
                isNestedScrollingEnabled = true
                layoutManager = GridLayoutManager(requireContext(), 2)
                this.adapter = adapter
            }
        }

        viewModel.videosLiveData.observe(viewLifecycleOwner)
        { videos ->
            videos.let { adapter.submitList(it) }
        }
    }
}