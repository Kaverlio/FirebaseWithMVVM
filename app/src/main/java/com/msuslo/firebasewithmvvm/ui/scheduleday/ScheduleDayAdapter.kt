package com.msuslo.firebasewithmvvm.ui.scheduleday

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.msuslo.firebasewithmvvm.data.model.Queue
import com.msuslo.firebasewithmvvm.databinding.ItemTimeBinding
import java.text.SimpleDateFormat
import java.util.*

class ScheduleDayAdapter(
    val onAddClicked: ((Int, Queue) -> Unit)? = null,
    val onDeleteClicked: ((Int, Queue) -> Unit)? = null,
) : RecyclerView.Adapter<ScheduleDayAdapter.MyViewHolder>() {

    private var list: MutableList<Queue> = arrayListOf()
    private var isDentist: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, position)
    }

    fun updateList(list: MutableList<Queue>){
        this.list = list
        list.sortBy{parseDate("${it.date} ${it.time}")}
        notifyDataSetChanged()
    }

    fun isDentist(status: Boolean) {
        isDentist = status
    }

    @SuppressLint("SimpleDateFormat")
    private fun parseDate (date: String): Long{
        val sdf = SimpleDateFormat("dd MMMM yyyy HH:mm")
        val dateTime: Date = sdf.parse(date)!!

        return dateTime.time
    }

    override fun getItemCount() = list.size

    inner class MyViewHolder(val binding: ItemTimeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Queue, position: Int){
            binding.tvDateTime.setText("${item.date} ${item.time}")

            if (isDentist){
                binding.btnAdd.visibility = View.GONE
                binding.btnDelete.visibility = View.VISIBLE

            } else {
                if (item.patient_id.isEmpty()){
                    binding.btnAdd.visibility = View.VISIBLE
                    binding.btnDelete.visibility = View.GONE
                } else {
                    binding.btnAdd.visibility = View.GONE
                    binding.btnDelete.visibility = View.VISIBLE
                }
            }
            binding.btnAdd.setOnClickListener {
                onAddClicked?.invoke(position, item)
            }
            binding.btnDelete.setOnClickListener {
                onDeleteClicked?.invoke(position, item)
            }
        }
    }
}