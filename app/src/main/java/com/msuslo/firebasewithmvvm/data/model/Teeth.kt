package com.msuslo.firebasewithmvvm.data.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Teeth(
    var id: String = "",
    var leftDown1: Int = 0,
    var leftDown2: Int = 0,
    var leftDown3: Int = 0,
    var leftDown4: Int = 0,
    var leftDown5: Int = 0,
    var leftDown6: Int = 0,
    var leftDown7: Int = 0,
    var leftDown8: Int = 0,
    var rightDown1: Int = 0,
    var rightDown2: Int = 0,
    var rightDown3: Int = 0,
    var rightDown4: Int = 0,
    var rightDown5: Int = 0,
    var rightDown6: Int = 0,
    var rightDown7: Int = 0,
    var rightDown8: Int = 0,
    var leftUp1: Int = 0,
    var leftUp2: Int = 0,
    var leftUp3: Int = 0,
    var leftUp4: Int = 0,
    var leftUp5: Int = 0,
    var leftUp6: Int = 0,
    var leftUp7: Int = 0,
    var leftUp8: Int = 0,
    var rightUp1: Int = 0,
    var rightUp2: Int = 0,
    var rightUp3: Int = 0,
    var rightUp4: Int = 0,
    var rightUp5: Int = 0,
    var rightUp6: Int = 0,
    var rightUp7: Int = 0,
    var rightUp8: Int = 0,
    var patient_id: String = ""
) : Parcelable