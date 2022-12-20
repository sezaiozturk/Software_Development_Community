package com.ktunsdc.software_development_community.viewmodel

import android.net.Uri
import android.text.BoringLayout
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.SetOptions
import com.ktunsdc.software_development_community.model.Gallery
import com.ktunsdc.software_development_community.model.Post
import com.ktunsdc.software_development_community.util.Singleton
import java.util.*

class ShareViewModel:ViewModel() {
    var load=MutableLiveData<Boolean>()
    var result=MutableLiveData<Boolean>()
    val uuid = UUID.randomUUID()
    val referance = Singleton.storage.reference.child("gallery").child("${uuid}.jpeg")
    val postReferance=Singleton.storage.reference.child("post").child("${uuid}.jpeg")
    fun shareGallery(uri: Uri, date: String?, comment: String?) {
        load.value=true
        referance.putFile(uri).addOnSuccessListener {
            referance.downloadUrl.addOnSuccessListener {
                val downloadUrl = it.toString()
                val gallery=Gallery(downloadUrl,date, Timestamp.now(),comment)
                Singleton.db.collection("Gallery").document().set(gallery, SetOptions.merge()).addOnSuccessListener {
                    load.value=false
                    result.value=true
                }.addOnFailureListener {
                    load.value=false
                    result.value=false
                }
            }
        }
    }
    fun sharePost(uri: Uri, comment: String?){
        load.value=true
        postReferance.putFile(uri).addOnSuccessListener {
            postReferance.downloadUrl.addOnSuccessListener {
                val downloadUrl = it.toString()
                val uuid=UUID.randomUUID().toString()
                val post=Post(uuid,Singleton.auth.currentUser!!.uid,downloadUrl,Timestamp.now(),comment,"0")
                Singleton.db.collection("Post").document().set(post, SetOptions.merge()).addOnSuccessListener {
                    load.value=false
                    result.value=true
                }.addOnFailureListener {
                    load.value=false
                    result.value=false
                }
            }
        }
    }
}