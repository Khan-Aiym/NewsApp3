package com.example.homework41.ui.notifications

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.fragment.app.Fragment
import com.example.homework41.databinding.FragmentNotificationsBinding
import com.google.firebase.storage.FirebaseStorage

class NotificationsFragment : Fragment() {
    private val notificationsViewModel: NotificationsViewModel? = null
    private var binding: FragmentNotificationsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.imageview.setOnClickListener { v: View? -> activityResultLauncher.launch("image/*") }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    var activityResultLauncher = registerForActivityResult(
        GetContent()
    ) { result ->
        if (result != null) {
            binding!!.imageview.setImageURI(result)
            upload(result)
        }
    }

    private fun upload(result: Uri) {
        val ref = FirebaseStorage.getInstance().reference.child("avatar.jpg")
        ref.putFile(result).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Uploaded", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Failed: " + task.exception!!.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
                task.exception!!.localizedMessage?.let { Log.e("Notif", it) }
            }
        }
    }
}