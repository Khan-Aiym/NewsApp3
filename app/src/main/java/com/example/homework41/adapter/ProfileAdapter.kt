package com.example.homework41.adapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.homework41.Model.Model
import com.example.homework41.OnClick
import com.example.homework41.adapter.ProfileAdapter.ProfileViewHolder
import com.example.homework41.databinding.ItemProfileBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Function

class ProfileAdapter() : RecyclerView.Adapter<ProfileViewHolder>() {
    private var data: MutableList<Model?>
    private var onClick: OnClick? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(view)
    }

    fun setData(list: MutableList<Model?>) {
        data = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(data[position])
        if (position % 2 == 0) {
            holder.binding.root.setBackgroundColor(Color.GRAY)
        } else {
            holder.binding.root.setBackgroundColor(Color.CYAN)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addItem(s: Model?) {
        data.add(0, s)
        notifyItemInserted(data.indexOf(s))
    }

    fun setOnClickListener(onClick: OnClick?) {
        this.onClick = onClick
    }

    fun getItem(position: Int): Model? {
        return data[position]
    }

    fun updateItem(s: Model?, position: Int) {
        data[position] = s
        notifyItemChanged(position)
    }

    fun deleteItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun addList(list: MutableList<Model?>) {
        val comparator = Comparator.comparing(
            Function<Model, Long> { model -> model.create })
        data = list
        data.sortWith(comparator)
        Collections.reverse(data)
        notifyDataSetChanged()
    }

    inner class ProfileViewHolder(val binding: ItemProfileBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(s: Model?) {
            if (s != null) {
                binding.title.text = s.title
            }
            val simpleDateFormat = SimpleDateFormat("HH:mm:ss, dd MMM yyyy", Locale.ROOT)
            val date = simpleDateFormat.format(s!!.create).toString()
            binding.tvDate.text = date
        }

        init {
            binding.root.setOnLongClickListener(object : OnLongClickListener {
                override fun onLongClick(view: View): Boolean {
                    onClick!!.onLongClick(adapterPosition)
                    return true
                }
            })
            binding.root.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    onClick!!.onClick(adapterPosition)
                }
            })
        }
    }

    init {
        data = ArrayList()
    }
}