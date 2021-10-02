package com.devbytes.app.igvideosaver.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devbytes.app.igvideosaver.R
import com.devbytes.app.igvideosaver.SocialGrabApplication
import com.devbytes.app.igvideosaver.databinding.FragmentVideosBinding
import com.devbytes.app.igvideosaver.viewmodels.VideoViewModel
import com.devbytes.app.igvideosaver.viewmodels.VideoViewModelFactory
import com.devbytes.app.igvideosaver.views.fragments.adapters.VideoListAdapter

class Videos : Fragment() {

    private var _binding: FragmentVideosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VideoViewModel by viewModels { VideoViewModelFactory((requireActivity().application as SocialGrabApplication).repository) }

    companion object {
        @JvmStatic
        fun newInstance() =
            Videos()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

    private fun configure() {
        viewModel.get()

        val adapter = VideoListAdapter(requireContext())

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