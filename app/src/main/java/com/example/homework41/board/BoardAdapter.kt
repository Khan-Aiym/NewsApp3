package com.example.homework41.board

import android.app.Activity
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.homework41.Model.Board
import com.example.homework41.R
import com.example.homework41.board.BoardAdapter.BoardViewHolder
import com.example.homework41.databinding.PagerBoardBinding

class BoardAdapter : RecyclerView.Adapter<BoardViewHolder>() {
    private val list: ArrayList<Board> = ArrayList()
    private val lottie = intArrayOf(R.raw.city, R.raw.city, R.raw.city)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = PagerBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class BoardViewHolder(private val binding: PagerBoardBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(position: Int) {
            val board = list[position]
            binding.textTitle.text = board.title
            binding.description.text = board.desc
            binding.animationView.setAnimation(lottie[position])
            Handler().postDelayed({ }, 2000)
            if (position == list.size - 1) {
                binding.btnStart.visibility = View.VISIBLE
            } else {
                binding.btnStart.visibility = View.INVISIBLE
            }
            binding.btnStart.setOnClickListener { view ->
                val navController = findNavController(
                    (view.context as Activity),
                    R.id.nav_host_fragment_activity_main
                )
                navController.navigateUp()
            }
        }
    }

    init {
        list.add(Board("Name", "Rustam"))
        list.add(Board("Name", "Rustam"))
        list.add(Board("Name", "Rustam"))
    }
}