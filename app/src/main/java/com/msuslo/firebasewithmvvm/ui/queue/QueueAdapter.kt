package com.msuslo.firebasewithmvvm.ui.queue

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.msuslo.firebasewithmvvm.data.model.Queue
import com.msuslo.firebasewithmvvm.data.model.User
import com.msuslo.firebasewithmvvm.databinding.QueueItemBinding
import java.text.SimpleDateFormat
import java.util.*

class QueueAdapter(
    val onDeleteClicked: ((Int, Queue) -> Unit)? = null
) : RecyclerView.Adapter<QueueAdapter.MyViewHolder>() {

    private var list: MutableList<Queue> = arrayListOf()
    private var listUsers: MutableList<User> = arrayListOf()

    var name: String = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = QueueItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateListQueue(list: MutableList<Queue>) {
        this.list = list
        list.sortBy{parseDate("${it.date} ${it.time}")}
        notifyDataSetChanged()
    }

    @SuppressLint("SimpleDateFormat")
    private fun parseDate (date: String): Long{
        val sdf = SimpleDateFormat("dd MMMM yyyy HH:mm")
        val dateTime: Date = sdf.parse(date)!!

        return dateTime.time
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateListUsers(list: MutableList<User>) {
        this.listUsers = list
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: QueueItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Queue, position: Int) {
            if (listUsers.isEmpty())
                binding.name.setText(name)
            else{
                val user = listUsers.find {
                    it.id == item.patient_id
                }
                name = "${user?.first_name ?: "Free"} ${user?.last_name ?: ""}"
                binding.name.setText(name)
            }

            binding.date.setText("Date: ${item.date}")
            binding.time.setText("Time: ${item.time}")
            binding.delete.setOnClickListener {
                onDeleteClicked?.invoke(position, item)
            }
        }
    }
}