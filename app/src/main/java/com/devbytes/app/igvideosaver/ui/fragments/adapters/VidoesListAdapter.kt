package com.devbytes.app.igvideosaver.ui.fragments.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devbytes.app.igvideosaver.data.entites.InstagramMedia
import com.devbytes.app.igvideosaver.databinding.RecyclerviewItemVideoGridBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class VidoesListAdapter(private val context: Context) : ListAdapter<InstagramMedia, VidoesListAdapter.VideoViewHolder>(VideoComparator()) {

    init {
        hasStableIds()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder = VideoViewHolder.create(context, parent)

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) = holder.bind(getItem(position))

    class VideoViewHolder(private val binding: RecyclerviewItemVideoGridBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: InstagramMedia) {
            with(binding) {
                caption.text = entity.caption
                entity.timestamp.let {
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

    class VideoComparator : DiffUtil.ItemCallback<InstagramMedia>() {
        override fun areItemsTheSame(oldItem: InstagramMedia, newItem: InstagramMedia): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: InstagramMedia, newItem: InstagramMedia): Boolean {
            return oldItem.id == newItem.id
        }
    }

}