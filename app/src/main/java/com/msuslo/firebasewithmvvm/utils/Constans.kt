package com.msuslo.firebasewithmvvm.utils

object FireStoreCollection{
    val NOTE = "note"
    val USER = "user"
    val HISTORYSICK = "historysick"
    val TEETH = "teeth"
}

object FireDatabase{
    val TASK = "task"
    val QUEUE = "queue"
}

object FireStoreDocumentField {
    val DATE = "date"
    val USER_ID = "user_id"
    val STATUS = "status"
}

object SharedPrefConstants {
    val LOCAL_SHARED_PREF = "local_shared_pref"
    val USER_SESSION = "user_session"
}

object FirebaseStorageConstants {
    val ROOT_DIRECTORY = "app"
    val NOTE_IMAGES = "note"
    val USER_IMAGES = "user"
}

enum class HomeTabs(val index: Int, val key: String) {
    NOTES(0, "notes"),
    TASKS(1, "tasks"),
    PROFILE(2, "profile"),
    QUEUE(3, "queue"),
}