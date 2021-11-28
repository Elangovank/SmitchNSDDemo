package com.smitch.nsddemo.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smitch.nsddemo.databinding.InflateAvailableServiceItemBinding


class AvailableServiceAdapter(
    var mServicetDataAL: List<ServiceModel>
) : RecyclerView.Adapter<AvailableServiceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return ViewHolder.from(parent)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mServicetDataAL.get(position)
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return mServicetDataAL.size
    }

    class ViewHolder private constructor(val binding: InflateAvailableServiceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ServiceModel) {
            binding.data = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    InflateAvailableServiceItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}
