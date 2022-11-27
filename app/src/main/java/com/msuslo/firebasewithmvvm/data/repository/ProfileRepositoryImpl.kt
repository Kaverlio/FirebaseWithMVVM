package com.msuslo.firebasewithmvvm.data.repository

import android.net.Uri
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.msuslo.firebasewithmvvm.data.model.Queue
import com.msuslo.firebasewithmvvm.data.model.User
import com.msuslo.firebasewithmvvm.utils.FireStoreCollection
import com.msuslo.firebasewithmvvm.utils.FireStoreDocumentField
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

    override fun getUsers(user: User?, result: (UiState<User>) -> Unit) {
        database.collection(FireStoreCollection.USER)
            .document(user?.id.toString())
            .get()
            .addOnSuccessListener {
                val obj = it.toObject(User::class.java)

                result.invoke(
                    UiState.Success(obj!!)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }

    }

    override fun getFullNameById(id: String, result: (UiState<String>) -> Unit){
        database.collection(FireStoreCollection.USER)
            .document(id)
            .get()
            .addOnSuccessListener {
                val obj = it.toObject(User::class.java)

                result.invoke(
                    UiState.Success("${obj?.first_name} ${obj?.last_name}")
                )

            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun getUsersByStatus(status: String, result: (UiState<List<User>>) -> Unit) {
        database.collection(FireStoreCollection.USER)
            .whereEqualTo(FireStoreDocumentField.STATUS, status)
            .get()
            .addOnSuccessListener {
                val users = arrayListOf<User>()
                for (document in it) {
                    val user = document.toObject(User::class.java)
                    users.add(user)
                }
                result.invoke(
                    UiState.Success(users)
                )
            }
            .addOnFailureListener {
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
                storageReference.child(FirebaseStorageConstants.USER_IMAGES)
                    .child(fileUri.lastPathSegment ?: "${System.currentTimeMillis()}")
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
                        storageReference.child(FirebaseStorageConstants.USER_IMAGES)
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