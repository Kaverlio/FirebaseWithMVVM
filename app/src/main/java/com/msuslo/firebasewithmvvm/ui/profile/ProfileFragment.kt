package com.msuslo.firebasewithmvvm.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.msuslo.firebasewithmvvm.R
import com.msuslo.firebasewithmvvm.data.model.User
import com.msuslo.firebasewithmvvm.databinding.FragmentProfileBinding
import com.msuslo.firebasewithmvvm.ui.auth.AuthViewModel
import com.msuslo.firebasewithmvvm.ui.note.ImageListingAdapter
import com.msuslo.firebasewithmvvm.ui.note.NoteListingFragment
import com.msuslo.firebasewithmvvm.utils.UiState
import com.msuslo.firebasewithmvvm.utils.hide
import com.msuslo.firebasewithmvvm.utils.show
import com.msuslo.firebasewithmvvm.utils.toast
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    var param1: String? = null
    lateinit var binding: FragmentProfileBinding
    val viewModel: ProfileViewModel by viewModels()
    val authViewModel: AuthViewModel by viewModels()
    var imageUris: MutableList<Uri> = arrayListOf()
    val adapter by lazy {
        ImageProfileAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (this::binding.isInitialized) {
            return binding.root
        } else {
            binding = FragmentProfileBinding.inflate(layoutInflater)
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.getSession {
            binding.tvName.setText("${R.string.name}: ${it?.first_name}")
            binding.tvLastName.setText("${R.string.lastname}: ${it?.last_name}")
            binding.tvSex.setText("${R.string.sex}: ${it?.sex}")
            binding.tvPhone.setText("${R.string.phone}: ${it?.phoneNum}")
            binding.tvAge.setText("${R.string.age}: ${it?.age.toString()}")
//            binding.ivProfile.setImageURI(it?.profileImg?.toUri())
//            imageUris = it?.images?.map { it.toUri() }?.toMutableList() ?: arrayListOf()
        }
        updateUI()
    }


    private fun updateUI() {
        binding.images.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.images.adapter = adapter
        binding.images.itemAnimator = null

//        adapter.updateList(imageUris)
//        binding.addImageLl.setOnClickListener {
//            binding.progressBar.show()
//            ImagePicker.with(this)
//                //.crop()
//                .compress(1024)
//                .galleryOnly()
//                .createIntent { intent ->
//                    startForProfileImageResult.launch(intent)
//                }
//        }

    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}