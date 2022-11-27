package com.msuslo.firebasewithmvvm.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.msuslo.firebasewithmvvm.data.model.Queue
import com.msuslo.firebasewithmvvm.data.model.User
import com.msuslo.firebasewithmvvm.utils.FireDatabase
import com.msuslo.firebasewithmvvm.utils.UiState

class QueueRepositoryImp(
    val database: FirebaseDatabase
) : IQueueRepository {

    override fun addRecord(queue: Queue, result: (UiState<Pair<Queue, String>>) -> Unit) {
        val reference = database.reference.child(FireDatabase.QUEUE).push()
        val uniqueKey = reference.key ?: "invalid"
        queue.id = uniqueKey
        reference.setValue(queue)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(Pair(queue, "Record has been created successfull"))
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

    override fun deleteRecord(queue: Queue, result: (UiState<Pair<Queue, String>>) -> Unit) {
        val reference = database.reference.child(FireDatabase.QUEUE).child(queue.id)
        reference.removeValue().addOnSuccessListener {
            result.invoke(
                UiState.Success(Pair(queue, "Record has been deleted successfully"))
            )
        }.addOnFailureListener {
            result.invoke(
                UiState.Failure(
                    it.localizedMessage
                )
            )
        }

    }

    override fun updateRecord(queue: Queue, result: (UiState<Pair<Queue, String>>) -> Unit) {
        val reference = database.reference.child(FireDatabase.QUEUE).child(queue.id)
        reference.setValue(queue).addOnSuccessListener {
            result.invoke(
                UiState.Success(Pair(queue, "Queue has been updated successfully"))
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

    override fun getRecordForDentist(user: User?, result: (UiState<List<Queue>>) -> Unit) {
        val reference = database.reference.child(FireDatabase.QUEUE)
        reference.get().addOnSuccessListener {
            val queue = arrayListOf<Queue?>()
            for (item in it.children) {
                val queueTemp = item.getValue(Queue::class.java)
                if (queueTemp?.dentist_id == user?.id)
                    queue.add(queueTemp)
            }
            result.invoke(UiState.Success(queue.filterNotNull()))
        }.addOnFailureListener {
            result.invoke(
                UiState.Failure(
                    it.localizedMessage
                )
            )
        }
    }

    override fun getRecord(user: User?, result: (UiState<List<Queue>>) -> Unit) {
        val reference = database.reference.child(FireDatabase.QUEUE)
        reference.get().addOnSuccessListener {
            val queue = arrayListOf<Queue?>()
            for (item in it.children) {
                val queueTemp = item.getValue(Queue::class.java)
                    if (user == null)
                    queue.add(queueTemp)
                else {
                    if(queueTemp?.patient_id == user.id)
                        queue.add(queueTemp)
                }
            }
            result.invoke(UiState.Success(queue.filterNotNull()))
        }.addOnFailureListener {
            result.invoke(
                UiState.Failure(
                    it.localizedMessage
                )
            )
        }
    }

    override fun getQueuesByDay(day: String, result: (UiState<List<Queue>>) -> Unit){
        val reference = database.reference.child(FireDatabase.QUEUE)
        reference.get().addOnSuccessListener {
            val queue = arrayListOf<Queue?>()
            for (item in it.children) {
                val queueTemp = item.getValue(Queue::class.java)
                if (queueTemp?.date == day)
                    queue.add(queueTemp)
            }
            result.invoke(UiState.Success(queue.filterNotNull()))
        }.addOnFailureListener {
            result.invoke(
                UiState.Failure(
                    it.localizedMessage
                )
            )
        }
    }
}