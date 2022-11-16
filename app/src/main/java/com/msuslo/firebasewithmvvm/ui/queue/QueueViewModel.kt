package com.msuslo.firebasewithmvvm.ui.queue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.msuslo.firebasewithmvvm.data.model.Queue
import com.msuslo.firebasewithmvvm.data.model.User
import com.msuslo.firebasewithmvvm.data.repository.IQueueRepository
import com.msuslo.firebasewithmvvm.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QueueViewModel @Inject constructor(
    val repository: IQueueRepository
) : ViewModel() {

    private val _addRecord = MutableLiveData<UiState<Pair<Queue, String>>>()
    val addRecord: LiveData<UiState<Pair<Queue, String>>>
        get() = _addRecord

    private val _deleteRecord = MutableLiveData<UiState<Pair<Queue, String>>>()
    val deleteRecord: LiveData<UiState<Pair<Queue, String>>>
        get() = _deleteRecord

    private val _getRecord = MutableLiveData<UiState<List<Queue>>>()
    val getRecord: LiveData<UiState<List<Queue>>>
        get() = _getRecord

    fun addRecord(queue: Queue) {
        _addRecord.value = UiState.Loading
        repository.addRecord(queue) { _addRecord.value = it }
    }

    fun deleteRecord(queue: Queue) {
        _deleteRecord.value = UiState.Loading
        repository.deleteRecord(queue) { _deleteRecord.value = it }
    }

    fun getRecord(user: User?){
        _getRecord.value = UiState.Loading
        repository.getRecord(user) { _getRecord.value = it }
    }

}