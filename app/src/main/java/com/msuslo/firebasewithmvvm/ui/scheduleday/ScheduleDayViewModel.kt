package com.msuslo.firebasewithmvvm.ui.scheduleday

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
class ScheduleDayViewModel @Inject constructor(
    val repository: IQueueRepository
) : ViewModel() {

    private val _queue = MutableLiveData<UiState<List<Queue>>>()
    val queue: LiveData<UiState<List<Queue>>>
        get() = _queue

    private val _addQueue = MutableLiveData<UiState<Pair<Queue, String>>>()
    val addQueue: LiveData<UiState<Pair<Queue, String>>>
        get() = _addQueue

    private val _updateQueue = MutableLiveData<UiState<Pair<Queue, String>>>()
    val updateQueue: LiveData<UiState<Pair<Queue, String>>>
        get() = _updateQueue

    private val _deleteQueue = MutableLiveData<UiState<Pair<Queue, String>>>()
    val deleteQueue: LiveData<UiState<Pair<Queue, String>>>
        get() = _deleteQueue

    fun addQueue(queue: Queue){
        _addQueue.value = UiState.Loading
        repository.addRecord(queue) { _addQueue.value = it }
    }

    fun updateQueue(queue: Queue){
        _updateQueue.value = UiState.Loading
        repository.updateRecord(queue) { _updateQueue.value = it }
    }

    fun getQueues(user: User?){
        _queue.value = UiState.Loading
        repository.getRecordForDentist(user) { _queue.value = it }
    }

    fun deleteQueue(queue: Queue) {
        _deleteQueue.value = UiState.Loading
        repository.deleteRecord(queue) { _deleteQueue.value = it }
    }
}