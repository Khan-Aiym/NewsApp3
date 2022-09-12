package com.example.homework41

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.homework41.Model.Model
import com.example.homework41.databinding.FragmentNewsBinding
import com.google.firebase.firestore.*

class NewsFragment constructor() : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    private lateinit var model: Model
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection("news")
    private val reference: DocumentReference = db.document("news/latest news")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = requireArguments().getSerializable("update") as Model
        if (model != null) binding.edittext.setText(model.title)
        binding.btnSave.setOnClickListener {
            if (binding.edittext.text.toString().isEmpty()) {
                YoYo.with(Techniques.Shake).duration(700).repeat(3).playOn(binding.edittext)
            }
            save()
        }
    }

    private fun save() {
        val bundle: Bundle = Bundle()
        val text: String = binding.edittext.text.toString().trim { it <= ' ' }
        if (text.isEmpty()) {
            Toast.makeText(requireContext(), "type task!", Toast.LENGTH_SHORT).show()
            return
        }
        if (model == null) {
            model = Model(System.currentTimeMillis(), text)
            App.Companion.dataBase?.newsDao()?.insert(model)
            saveToFirestore(model!!)
        } else {
            model.title = text
        }
        bundle.putSerializable("model", model)
        parentFragmentManager.setFragmentResult("rk_news", bundle)
        close()
    }

    private fun saveToFirestore(news: Model) {
        val map: MutableMap<String, Any> = HashMap()
        map["123"] = news
        db.collection("user")
            .add(map)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "TAG",
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w("TAG", "Error adding document", e) }
        close()
    }

    private fun close() {
        val navController: NavController =
            findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
        navController.navigateUp()
    }
}

