package com.example.homework41.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.homework41.Model.Model
import com.example.homework41.adapter.DashboardAdapter
import com.example.homework41.databinding.FragmentDashboardBinding
import com.google.firebase.firestore.FirebaseFirestore

class DashboardFragment : Fragment() {
    private var dashboardViewModel: DashboardViewModel? = null
    private var binding: FragmentDashboardBinding? = null
    private val list = ArrayList<Model>()
    private var adapter: DashboardAdapter? = null
    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]
        binding = FragmentDashboardBinding.inflate(
            inflater,
            container,
            false
        )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getToFireStore(list)
    }

    private fun getToFireStore(models: ArrayList<Model>) {
        firestore.collection("news")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.d("TAG", document.id + " => " + document.data)
                        val modelss = document.toObject(
                            Model::class.java
                        )
                        models.add(modelss)
                    }
                    setAdapter()
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }
    }

    private fun setAdapter() {
        adapter = DashboardAdapter()
        binding!!.recNot.adapter = adapter
        adapter!!.setList(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}