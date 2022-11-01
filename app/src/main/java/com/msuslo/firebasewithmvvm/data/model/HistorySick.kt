package com.msuslo.firebasewithmvvm.data.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class HistorySick(
    var id: String = "",
    @ServerTimestamp
    val date: Date = Date(),
    val description: String = "",
    val patient_id: String = ""
) : Parcelable