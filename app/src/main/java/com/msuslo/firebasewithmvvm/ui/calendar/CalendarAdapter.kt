package com.msuslo.firebasewithmvvm.ui.calendar

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.msuslo.firebasewithmvvm.data.model.Queue
import com.msuslo.firebasewithmvvm.data.model.Task
import com.msuslo.firebasewithmvvm.databinding.CalendarCellBinding
import com.msuslo.firebasewithmvvm.databinding.TaskLayoutBinding

class CalendarAdapter(
    val onItemClicked: ((Int, String) -> Unit)? = null
) : RecyclerView.Adapter<CalendarAdapter.MyViewHolder>() {

    private var list: MutableList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = CalendarCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var item = list[position]
        holder.bind(item, position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: MutableList<String>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: CalendarCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, position: Int) {
            if (item.isNullOrEmpty())
                binding.root.isVisible = false
            binding.cellDayText.setText(item)
            binding.cellDayText.setOnClickListener {
                onItemClicked?.invoke(position, item)
            }
        }
    }
}