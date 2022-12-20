package com.ktunsdc.software_development_community.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.ktunsdc.software_development_community.model.Gallery
import com.ktunsdc.software_development_community.util.Singleton

class PhotoViewmodel:ViewModel() {
    val photoList=MutableLiveData<ArrayList<Gallery>>()
    fun getPhotos(){
        var photos=ArrayList<Gallery>()
        Singleton.db.collection("Gallery").orderBy("firebaseDate",Query.Direction.DESCENDING).get().addOnSuccessListener {
            val documents=it.documents
            for(document in documents)
            {
                var photo=Gallery(document["galleryPhotoUrl"] as String,document["date"] as String?,document["firebaseDate"] as Timestamp,document["comment"] as String?)
                photos.add(photo)
            }
            photoList.value=photos
        }
    }
}