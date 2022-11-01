package com.msuslo.firebasewithmvvm.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.msuslo.firebasewithmvvm.data.model.User
import com.msuslo.firebasewithmvvm.data.repository.IProfileRepository
import com.msuslo.firebasewithmvvm.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val repository: IProfileRepository
) : ViewModel() {
    private val _user = MutableLiveData<UiState<User>>()
    val user: LiveData<UiState<User>>
        get() = _user
}