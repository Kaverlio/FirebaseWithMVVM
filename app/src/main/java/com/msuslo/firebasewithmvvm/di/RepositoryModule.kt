package com.msuslo.firebasewithmvvm.di

import com.google.firebase.firestore.FirebaseFirestore
import com.msuslo.firebasewithmvvm.data.repository.INoteRepository
import com.msuslo.firebasewithmvvm.data.repository.NoteRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideNoteRepository(
        database: FirebaseFirestore
    ): INoteRepository{
        return NoteRepositoryImp(database)
    }
}