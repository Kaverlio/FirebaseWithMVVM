package com.msuslo.firebasewithmvvm.data.repository

import android.net.Uri
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.msuslo.firebasewithmvvm.data.model.User
import com.msuslo.firebasewithmvvm.utils.FireStoreCollection
import com.msuslo.firebasewithmvvm.utils.FirebaseStorageConstants
import com.msuslo.firebasewithmvvm.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(
    val database: FirebaseFirestore,
    val storageReference: StorageReference
) : IProfileRepository {
    override fun updateUser(user: User, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreCollection.USER).document(user.id)
        document.set(user).addOnSuccessListener {
            result.invoke(
                UiState.Success("Your data has been update successfully")
            )
        }.addOnFailureListener {
            result.invoke(
                UiState.Failure(
                    it.localizedMessage
                )
            )
        }
    }

    override suspend fun uploadImageProfile(fileUri: Uri, onResult: (UiState<Uri>) -> Unit) {
        try {
            val uri: Uri = withContext(Dispatchers.IO) {
                storageReference
                    .putFile(fileUri)
                    .await()
                    .storage
                    .downloadUrl
                    .await()
            }
            onResult.invoke(UiState.Success(uri))
        } catch (e: FirebaseException) {
            onResult.invoke(UiState.Failure(e.message))
        } catch (e: Exception) {
            onResult.invoke(UiState.Failure(e.message))
        }
    }

    override suspend fun uploadImages(filesUri: List<Uri>, onResult: (UiState<List<Uri>>) -> Unit) {
        try {
            val uri: List<Uri> = withContext(Dispatchers.IO) {
                filesUri.map { image ->
                    async {
                        //TODO SAVE PICTURES DOCTOR AND PATIENT
                        storageReference.child(FirebaseStorageConstants.XRAY_PATIENT)
                            .child(image.lastPathSegment ?: "${System.currentTimeMillis()}")
                            .putFile(image)
                            .await()
                            .storage
                            .downloadUrl
                            .await()
                    }
                }.awaitAll()
            }
            onResult.invoke(UiState.Success(uri))
        } catch (e: FirebaseException) {
            onResult.invoke(UiState.Failure(e.message))
        } catch (e: Exception) {
            onResult.invoke(UiState.Failure(e.message))
        }
    }
}