package com.example.homework41

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.homework41.Model.Model
import com.example.homework41.adapter.ProfileAdapter
import com.example.homework41.databinding.FragmentProfileBinding

class ProfileFragment constructor() : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var prefs: Prefs
    private val adapter: ProfileAdapter? = null
    private val models: ArrayList<Model>? = null
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentProfileBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = Prefs(requireContext())
        binding.etName.setText(prefs.name)
        binding.etName.addTextChangedListener(object : TextWatcher {
            public override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {
                prefs.saveName(binding.etName.text.toString())
            }
        })
        binding.imageView.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            activityResultLauncher.launch(intent)
        }
    }

    private var activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                uri = result.data!!.getData()
                prefs.saveAvatar(uri.toString())
                binding.imageView.setImageURI(uri)
            }
        }

    override fun onStart() {
        super.onStart()
        if (prefs.avatar != null) {
            uri = Uri.parse(prefs.avatar)
            Glide.with(requireContext()).load(uri).circleCrop().into(binding.imageView)
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        Glide.with(requireContext()).load(uri).circleCrop().into(binding.imageView)
    }
}