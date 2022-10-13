package com.msuslo.firebasewithmvvm.di

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.msuslo.firebasewithmvvm.data.repository.*
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
        database: FirebaseFirestore,
        storageReference: StorageReference
    ): INoteRepository{
        return NoteRepositoryImp(database,storageReference)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        database: FirebaseDatabase
    ): ITaskRepository {
        return TaskRepositoryImp(database)
    }

    @Provides
    @Singleton
    fun provideAutghRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth,
        appPreferences: SharedPreferences,
        gson: Gson
    ): IAuthRepository {
        return AuthRepositoryImp(auth,database,appPreferences,gson)
    }
}