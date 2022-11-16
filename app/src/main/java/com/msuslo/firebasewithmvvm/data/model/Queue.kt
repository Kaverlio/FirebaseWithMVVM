package com.msuslo.firebasewithmvvm.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Queue(
    var id: String = "",
    var date: String = "",
    var time: String = "",
    var patient_id: String = "",
    var dentist_id: String = "",
) : Parcelable