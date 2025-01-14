package com.example.cleansound.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cleansound.databinding.ItemTrackBinding
import com.example.cleansound.model.search.ItemsItem

class SearchTracksAdapter(private val onTrackClicked: (String) -> Unit) : ListAdapter<ItemsItem, SearchTracksAdapter.SearchTracksViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<ItemsItem>() {
        override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
            return oldItem == newItem
        }

    }

    class SearchTracksViewHolder(private val binding: ItemTrackBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemsItem: ItemsItem) {
            binding.tvTrackTitle.text = itemsItem.name
            binding.tvTrackArtist.text = itemsItem.artists?.joinToString { it?.name.orEmpty() }
            itemsItem.album?.images?.firstOrNull()?.url.let {imgUrl ->
                Glide.with(binding.root.context).load(imgUrl).into(binding.ivTrackImage)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchTracksViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context))
        return SearchTracksViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SearchTracksViewHolder,
        position: Int
    ) {
        getItem(position)?.let {track ->
            holder.itemView.setOnClickListener{
                onTrackClicked(track.id!!)
            }
            holder.bind(track)
        }
    }
}