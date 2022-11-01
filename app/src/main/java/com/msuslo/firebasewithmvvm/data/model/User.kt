package com.msuslo.firebasewithmvvm.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val email: String = "",
    val phoneNum: String = "",
    val age: Int = 0,
    val sex: String = "",
    val profileImg: String = "",
    val images: List<String> = arrayListOf(),
    val status: String = "",
) : Parcelable