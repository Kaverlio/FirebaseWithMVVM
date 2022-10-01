package com.msuslo.firebasewithmvvm.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.msuslo.firebasewithmvvm.data.model.Note
import com.msuslo.firebasewithmvvm.data.repository.INoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteViewModel
    @Inject constructor( private val repository: INoteRepository): ViewModel() {

    private val _notes = MutableLiveData<List<Note>>()
    val note: LiveData<List<Note>>
            get() = _notes

    fun getNotes() {
        _notes.value = repository.getNotes()
    }
}