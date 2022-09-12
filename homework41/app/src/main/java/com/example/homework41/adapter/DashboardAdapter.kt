package com.example.homework41.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.homework41.Model.Model
import com.example.homework41.adapter.DashboardAdapter.DashboardViewHolder
import com.example.homework41.databinding.ItemBinding
import java.text.SimpleDateFormat
import java.util.*

class DashboardAdapter constructor() : RecyclerView.Adapter<DashboardViewHolder>() {
    private var list: List<Model>? = null
    fun setList(list: List<Model>?) {
        this.list = list
        notifyDataSetChanged()
    }

    public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view: ItemBinding =
            ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
        return DashboardViewHolder(view)
    }

    public override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bind(list!!.get(position))
        if (position % 2 == 0) {
            holder.binding.getRoot().setBackgroundColor(Color.GRAY)
        } else {
            holder.binding.getRoot().setBackgroundColor(Color.CYAN)
        }
    }

    public override fun getItemCount(): Int {
        return list!!.size
    }

    class DashboardViewHolder constructor(val binding: ItemBinding) :
        RecyclerView.ViewHolder(
            binding.getRoot()
        ) {
        fun bind(s: Model) {
            binding.tvTitle.setText(s.title)
            val simpleDateFormat: SimpleDateFormat =
                SimpleDateFormat("HH:mm:ss, dd MMM yyyy", Locale.ROOT)
            val date: String = simpleDateFormat.format(s.create).toString()
            binding.tvDate.setText(date)
        }
    }
}