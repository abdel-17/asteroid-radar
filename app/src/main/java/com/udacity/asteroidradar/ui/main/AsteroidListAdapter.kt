package com.udacity.asteroidradar.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidItemBinding
import com.udacity.asteroidradar.api.Asteroid

class AsteroidListAdapter(
    private val onItemClick: (Asteroid) -> Unit
) : ListAdapter<Asteroid, AsteroidViewHolder>(AsteroidDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.fromParent(parent)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }
}

class AsteroidViewHolder private constructor(
    private val binding: AsteroidItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun fromParent(parent: ViewGroup): AsteroidViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = AsteroidItemBinding.inflate(inflater)
            return AsteroidViewHolder(binding)
        }
    }

    fun bind(item: Asteroid, onItemClick: (Asteroid) -> Unit) {
        binding.asteroid = item
        binding.root.setOnClickListener { onItemClick(item) }
        binding.executePendingBindings()
    }
}

private object AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem == newItem
    }
}