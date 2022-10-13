package com.msuslo.firebasewithmvvm.data.repository

import com.msuslo.firebasewithmvvm.data.model.Task
import com.msuslo.firebasewithmvvm.data.model.User
import com.msuslo.firebasewithmvvm.utils.UiState

interface ITaskRepository {
    fun addTask(task: Task, result: (UiState<Pair<Task, String>>) -> Unit)
    fun updateTask(task: Task, result: (UiState<Pair<Task,String>>) -> Unit)
    fun deleteTask(task: Task, result: (UiState<Pair<Task,String>>) -> Unit)
    fun getTasks(user: User?, result: (UiState<List<Task>>) -> Unit)
}