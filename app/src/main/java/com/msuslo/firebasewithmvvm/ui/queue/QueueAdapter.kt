package com.msuslo.firebasewithmvvm.ui.queue

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.msuslo.firebasewithmvvm.data.model.Queue
import com.msuslo.firebasewithmvvm.databinding.QueueItemBinding

class QueueAdapter(
    val onDeleteClicked: ((Int, Queue) -> Unit)? = null
) : RecyclerView.Adapter<QueueAdapter.MyViewHolder>() {

    private var list: MutableList<Queue> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = QueueItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item,position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: MutableList<Queue>){
        this.list = list
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        list.removeAt(position)
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: QueueItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Queue, position: Int) {
            binding.doctorName.setText("Set doctor name!")
            binding.date.setText("Date: ${item.date}")
            binding.time.setText("Time: ${item.time}")
            binding.delete.setOnClickListener {
                onDeleteClicked?.invoke(position,item)
            }
        }
    }
}