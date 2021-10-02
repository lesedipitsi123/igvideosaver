package com.devbytes.app.igvideosaver.views.fragments.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devbytes.app.igvideosaver.R
import com.devbytes.app.igvideosaver.data.entites.VideoEntity
import com.devbytes.app.igvideosaver.databinding.RecyclerviewItemVideoBinding
import com.devbytes.app.igvideosaver.databinding.RecyclerviewItemVideoGridBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class VideoListAdapter(private val context: Context) : ListAdapter<VideoEntity, VideoListAdapter.VideoViewHolder>(VideoComparator()) {

    init {
        hasStableIds()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder = VideoViewHolder.create(context, parent)

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) = holder.bind(getItem(position))

    class VideoViewHolder(private val binding: RecyclerviewItemVideoGridBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: VideoEntity) {
            with(binding) {
                caption.text = entity.caption
                entity.date?.let {
                    date.text =  SimpleDateFormat("dd MMMM", Locale.getDefault()).format(it)
                }
                Picasso.get()
                    .load(Uri.parse(entity.thumbnail))
                    .into(thumbnail)
            }
        }

        companion object {
            fun create(context: Context, parent: ViewGroup): VideoViewHolder {
                val binding = RecyclerviewItemVideoGridBinding.inflate(LayoutInflater.from(context), parent, false)
                return VideoViewHolder(binding)
            }
        }
    }

    class VideoComparator : DiffUtil.ItemCallback<VideoEntity>() {
        override fun areItemsTheSame(oldItem: VideoEntity, newItem: VideoEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: VideoEntity, newItem: VideoEntity): Boolean {
            return oldItem.link == newItem.link
        }
    }

}