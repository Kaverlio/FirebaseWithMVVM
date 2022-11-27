package com.msuslo.firebasewithmvvm.ui.updateprofile

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.msuslo.firebasewithmvvm.R
import com.msuslo.firebasewithmvvm.data.model.User
import com.msuslo.firebasewithmvvm.databinding.FragmentUpdateProfileBinding
import com.msuslo.firebasewithmvvm.ui.auth.AuthViewModel
import com.msuslo.firebasewithmvvm.ui.profile.ProfileViewModel
import com.msuslo.firebasewithmvvm.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProfileFragment : Fragment() {

    val TAG: String = "UpdateProfileFragment"
    lateinit var binding: FragmentUpdateProfileBinding
    val profileViewModel: ProfileViewModel by viewModels()
    val updateViewModel: UpdateProfileViewModel by viewModels()
    val authViewModel: AuthViewModel by viewModels()
    var objUser: User? = null
    var imageUris: MutableList<Uri> = arrayListOf()
    var imageProfile: Uri? = null
    val adapter by lazy {
        UpdateProfileAdapter(
            onCancelClicked = { pos, item -> onRemoveImage(pos, item) }
        )
    }

    private val startForProfileImagesResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                imageUris.add(fileUri)
                adapter.updateList(imageUris)
                binding.pbLoad.hide()
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                binding.pbLoad.hide()
                toast(ImagePicker.getError(data))
            } else {
                binding.pbLoad.hide()
            }
        }

    private val profileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                imageProfile = data?.data!!
                binding.ivProfile.setImageURI(data.data)
                binding.pbLoad.hide()
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                binding.pbLoad.hide()
                toast(ImagePicker.getError(data))
            } else {
                binding.pbLoad.hide()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (this::binding.isInitialized) {
            return binding.root
        } else {
            binding = FragmentUpdateProfileBinding.inflate(layoutInflater)
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.llAddImage.setOnClickListener {
            binding.pbLoad.show()
            ImagePicker.with(this)
                //.crop()
                .compress(1024)
                .galleryOnly()
                .createIntent { intent ->
                    startForProfileImagesResult.launch(intent)
                }
        }
        binding.ivProfile.setOnClickListener {
            binding.pbLoad.show()
            ImagePicker.with(this)
                .compress(1024)
                .galleryOnly()
                .createIntent { intent -> profileImageResult.launch(intent) }
        }
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnApply.setOnClickListener {
            if (validation()) {
                onApplyPressed()
            }
        }

        authViewModel.getSession {
            profileViewModel.getUser(it)
        }
        observer()

    }

    private fun observer() {
        profileViewModel.user.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    binding.pbLoad.show()
                }
                is UiState.Failure -> {
                    binding.pbLoad.hide()
                    toast(it.error)
                }
                is UiState.Success -> {
                    binding.pbLoad.hide()
                    updateUI(it.data)
                    objUser = it.data
                }
            }
        }
    }


    private fun updateUI(data: User) {
        binding.etName.setText(data.first_name)
        binding.ivProfile.setImageURI(data.profileImg.toUri())
        binding.etLastName.setText(data.last_name)
        binding.etAge.setText(data.age.toString())
        binding.etPhone.setText(data.phoneNum)

        if (data.profileImg.isNullOrEmpty())
            binding.ivProfile.setImageResource(R.drawable.ic_profile_24)

        val sexes = resources.getStringArray(R.array.Sex)
        val adapterSex = ArrayAdapter(requireContext(), R.layout.dropdown_item, sexes)
        binding.sSex.adapter = adapterSex

        binding.images.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.images.adapter = adapter
        binding.images.itemAnimator = null
        imageUris = data.images.map { it.toUri() }.toMutableList() ?: arrayListOf()
        adapter.updateList(imageUris)

    }

    private fun onApplyPressed() {
        if (imageUris.isNotEmpty()) {
            updateViewModel.onUploadProfileImage(imageProfile!!){}
            updateViewModel.onUploadMultipleFile(imageUris) { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.pbLoad.show()
                    }
                    is UiState.Failure -> {
                        binding.pbLoad.hide()
                        toast(state.error)
                    }
                    is UiState.Success -> {
                        binding.pbLoad.hide()
                        authViewModel.updateProfile(getUser())
                    }
                }
            }
        } else {
            authViewModel.updateProfile(getUser())
        }
    }

    private fun getUser(): User {
        return User(
            id = objUser?.id ?: "",
            first_name = binding.etName.text.toString(),
            last_name = binding.etLastName.text.toString(),
            email = objUser?.email.toString(),
            phoneNum = binding.etPhone.text.toString(),
            age = binding.etAge.text.toString().toInt(),
            sex = binding.sSex.selectedItem.toString(),
            profileImg = imageProfile.toString(),
            images = getImageUrls(),
            status = objUser?.status.toString(),
        )
    }

    private fun getImageUrls(): List<String> {
        if (imageUris.isNotEmpty()) {
            return imageUris.map { it.toString() }
        } else {
            return objUser?.images ?: arrayListOf()
        }
    }

    private fun validation(): Boolean {
        var isValid = true
        if (binding.etName.text.toString().isNullOrEmpty()) {
            isValid = false
            toast(getString(R.string.error_name))
        }
        if (binding.etLastName.text.toString().isNullOrEmpty()) {
            isValid = false
            toast(getString(R.string.error_last_name))
        }
        if (binding.etAge.text.toString().isNullOrEmpty()) {
            isValid = false
            toast(getString(R.string.error_age))
        }
        if (binding.etPhone.text.toString().isNullOrEmpty()) {
            isValid = false
            toast(getString(R.string.error_phone))
        }
        return isValid
    }

    private fun onRemoveImage(pos: Int, item: Uri) {
        adapter.removeItem(pos)
    }
}