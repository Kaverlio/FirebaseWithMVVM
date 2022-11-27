package com.msuslo.firebasewithmvvm.data.repository

import android.net.Uri
import com.msuslo.firebasewithmvvm.data.model.Queue
import com.msuslo.firebasewithmvvm.data.model.User
import com.msuslo.firebasewithmvvm.utils.UiState

interface IProfileRepository {
    fun getUsers(user: User?, result: (UiState<User>) -> Unit)
    fun getUsersByStatus(status: String, result: (UiState<List<User>>) -> Unit)
    fun getFullNameById(id: String, result: (UiState<String>) -> Unit)
    suspend fun uploadImageProfile(fileUri: Uri, onResult: (UiState<Uri>) -> Unit)
    suspend fun uploadImages(filesUri: List<Uri>, onResult: (UiState<List<Uri>>) -> Unit)
}