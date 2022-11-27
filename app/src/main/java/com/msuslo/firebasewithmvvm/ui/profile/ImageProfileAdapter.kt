package com.msuslo.firebasewithmvvm.ui.profile

import android.net.Uri
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.msuslo.firebasewithmvvm.databinding.ImageLayoutBinding

class ImageProfileAdapter() : RecyclerView.Adapter<ImageProfileAdapter.MyViewHolder>() {

    private var list: MutableList<Uri> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ImageLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageProfileAdapter.MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item,position)
    }

    fun updateList(list: MutableList<Uri>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ImageLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Uri, position: Int) {
            binding.image.setImageURI(item)
            binding.cancel.isVisible = false
        }
    }
}