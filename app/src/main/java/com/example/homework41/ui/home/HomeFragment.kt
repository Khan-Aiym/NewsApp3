package com.example.homework41.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.homework41.App
import com.example.homework41.Model.Model
import com.example.homework41.OnClick
import com.example.homework41.R
import com.example.homework41.adapter.ProfileAdapter
import com.example.homework41.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private var adapter: ProfileAdapter? = null
    private var isChanged = false
    private var position = 0
    var model: Model? = null
    private var list: MutableList<Model?> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        adapter = ProfileAdapter()
        App.dataBase?.newsDao()?.let { adapter!!.addList(it.all) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.fab.setOnClickListener {
            isChanged = false
            open(null)
        }
        binding!!.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                list = App.dataBase?.newsDao()!!.getSearch(editable.toString())
                adapter!!.addList(list)
            }
        })
        binding!!.recycleView.adapter = adapter
        list = App.dataBase?.newsDao()!!.sortAll()
        adapter!!.addList(list)
        parentFragmentManager.setFragmentResultListener(
            "rk_news",
            viewLifecycleOwner
        ) { requestKey, result ->
            val model = result.getSerializable("model") as Model?
            Log.e("Home", "text = " + model?.title)
            if (isChanged) adapter!!.updateItem(model, position) else adapter!!.addItem(model)
        }
        binding!!.recycleView.adapter = adapter
        adapter!!.setOnClickListener(object : OnClick {
            override fun onClick(position: Int) {
                model = adapter!!.getItem(position)
                isChanged = true
                open(model)
                this@HomeFragment.position = position
            }

            override fun onLongClick(position: Int) {
                AlertDialog.Builder(context).setTitle("Delete")
                    .setMessage("Вы уверены что хотите удалить?").setNegativeButton("Отмена", null)
                    .setPositiveButton("Да") { _, _ ->
                        model = adapter!!.getItem(position)
                        adapter!!.deleteItem(position)
                        App.dataBase?.newsDao()?.deleteTask(model)
                        adapter!!.notifyDataSetChanged()
                    }.show()
            }
        })
    }

    private fun open(model: Model?) {
        val navController =
            findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
        val bundle = Bundle()
        bundle.putSerializable("update", model)
        navController.navigate(R.id.newsFragment, bundle)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sort) {
            App.dataBase?.newsDao()?.let { adapter!!.setData(it.sort()) }
            adapter!!.notifyDataSetChanged()
        }
        return super.onOptionsItemSelected(item)
    }
}