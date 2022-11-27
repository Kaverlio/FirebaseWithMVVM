package com.msuslo.firebasewithmvvm.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.msuslo.firebasewithmvvm.R
import com.msuslo.firebasewithmvvm.data.model.User
import com.msuslo.firebasewithmvvm.databinding.FragmentProfileBinding
import com.msuslo.firebasewithmvvm.ui.auth.AuthViewModel
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
        observer()
        binding.btnUpdateProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_updateProfileFragment)
        }


        authViewModel.getSession {
            viewModel.getUser(it)
            binding.btnTeethStatus.isVisible = it?.status != "Dentist"
        }
    }

    private fun observer() {
        viewModel.user.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(it.error)
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    updateProfile(it.data)
                }
            }
        }

    }

    private fun updateProfile(data: User) {
        binding.tvName.text = "${getString(R.string.name)}: ${data?.first_name}"
        binding.tvLastName.text = "${getString(R.string.lastname)}: ${data?.last_name}"
        binding.tvSex.text = "${getString(R.string.sex)}: ${data?.sex}"
        binding.tvPhone.text = "${getString(R.string.phone)}: ${data?.phoneNum}"
        binding.tvAge.text = "${getString(R.string.age)}: ${data?.age.toString()}"
        binding.ivProfile.setImageURI(data.profileImg.toUri())
        if (data.profileImg.isNullOrEmpty())
            binding.ivProfile.setImageResource(R.drawable.ic_profile_24)

        imageUris = data.images.map { it.toUri() }.toMutableList() ?: arrayListOf()
        binding.images.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.images.adapter = adapter
        binding.images.itemAnimator = null

        adapter.updateList(imageUris)

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