package com.msuslo.firebasewithmvvm.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.msuslo.firebasewithmvvm.data.model.Note
import java.util.*

class NoteRepositoryImp(
    private val database: FirebaseFirestore
): INoteRepository {
    override fun getNotes(): List<Note> {
        return arrayListOf(
            Note(
                id = "1",
                text = "f",
                date = Date()
            ),

            Note(
                id = "2",
                text = "d",
                date = Date()
            ),

            Note(
                id = "3",
                text = "g",
                date = Date()
            )
        )
    }
}