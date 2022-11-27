package com.msuslo.firebasewithmvvm.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.msuslo.firebasewithmvvm.data.model.Queue
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

    private val _name = MutableLiveData<UiState<String>>()
    val name: LiveData<UiState<String>>
        get() = _name

    private val _users = MutableLiveData<UiState<List<User>>>()
    val users: LiveData<UiState<List<User>>>
        get() = _users

    fun getUser(u: User?) {
        _user.value = UiState.Loading
        repository.getUsers(u) { _user.value = it}
    }

    fun getUsersByStatus(status: String){
        repository.getUsersByStatus(status) { _users.value = it}
    }

    fun getFullNameById(id: String){

        repository.getFullNameById(id) { _name.value = it }

    }
}