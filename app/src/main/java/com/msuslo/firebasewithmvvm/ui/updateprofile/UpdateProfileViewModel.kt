package com.msuslo.firebasewithmvvm.ui.updateprofile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msuslo.firebasewithmvvm.data.repository.IProfileRepository
import com.msuslo.firebasewithmvvm.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    val repository: IProfileRepository
): ViewModel() {

    fun onUploadProfileImage(fileUri: Uri, onResult: (UiState<Uri>) -> Unit){
        onResult.invoke(UiState.Loading)
        viewModelScope.launch {
            repository.uploadImageProfile(fileUri,onResult)
        }
    }

    fun onUploadMultipleFile(fileUris: List<Uri>, onResult: (UiState<List<Uri>>) -> Unit){
        onResult.invoke(UiState.Loading)
        viewModelScope.launch {
            repository.uploadImages(fileUris,onResult)
        }
    }
}