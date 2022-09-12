package com.example.homework41.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.homework41.MainActivity
import com.example.homework41.R
import com.example.homework41.databinding.FragmentBoardBinding

class BoardFragment : Fragment() {
    private var binding: FragmentBoardBinding? = null
    private var adapter: BoardAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoardBinding.inflate(
            LayoutInflater.from(
                context
            ), container, false
        )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = BoardAdapter()
        binding!!.viewPager.adapter = adapter
        binding!!.springDotsIndicator.setViewPager2(binding!!.viewPager)
        binding!!.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position == 2) {
                    binding!!.skip.visibility = View.INVISIBLE
                } else {
                    binding!!.skip.visibility = View.VISIBLE
                }
            }
        })
        binding!!.skip.setOnClickListener { close() }
    }

    private fun close() {
        MainActivity.Companion.prefs!!.saveBoardState()
        val navController =
            findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
        navController.navigateUp()
    }
}