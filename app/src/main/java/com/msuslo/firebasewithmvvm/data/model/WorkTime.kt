package com.msuslo.firebasewithmvvm.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkTime(
    var id: String = "",
    val startWorck: String = "",
    val endWorck: String = "",
    val dentist_id: String = "",
) : Parcelable