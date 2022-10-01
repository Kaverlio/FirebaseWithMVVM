package com.msuslo.firebasewithmvvm.data.repository

import com.msuslo.firebasewithmvvm.data.model.Note

interface INoteRepository {
    fun getNotes(): List<Note>
}