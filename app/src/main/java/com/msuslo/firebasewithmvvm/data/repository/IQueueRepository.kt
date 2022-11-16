package com.msuslo.firebasewithmvvm.data.repository

import com.msuslo.firebasewithmvvm.data.model.Queue
import com.msuslo.firebasewithmvvm.data.model.User
import com.msuslo.firebasewithmvvm.utils.UiState

interface IQueueRepository {
    fun addRecord(queue: Queue, result: (UiState<Pair<Queue, String>>) -> Unit)
    fun deleteRecord(queue: Queue, result: (UiState<Pair<Queue, String>>) -> Unit)
    fun getRecord(user: User?, result: (UiState<List<Queue>>) -> Unit)
}