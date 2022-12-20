package com.ktunsdc.software_development_community.util

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

object Singleton{
    val auth=Firebase.auth
    val storage=Firebase.storage
    val db=Firebase.firestore
}