package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ListItemBinding

class MainViewHolder(private var binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(asteroid: Asteroid){
        binding.asteroid = asteroid
        binding.executePendingBindings()
    }
}

class MainAdapter(private val onClickListener: OnClickListener): ListAdapter<Asteroid, MainViewHolder>(MainDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(asteroid)
        }
        holder.bind(asteroid)
    }

    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit){
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}

class MainDiffCallback: DiffUtil.ItemCallback<Asteroid>(){
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem == newItem
    }
}

