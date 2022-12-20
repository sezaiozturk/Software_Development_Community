package com.ktunsdc.software_development_community.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.ktunsdc.software_development_community.model.Gallery
import com.ktunsdc.software_development_community.util.Singleton

class GalleryViewModel:ViewModel (){
    val photo=MutableLiveData<ArrayList<Gallery>>()

    fun getPhotos(){
        val list=ArrayList<Gallery>()
        Singleton.db.collection("Gallery").orderBy("firebaseDate",Query.Direction.DESCENDING).get().addOnSuccessListener {
            it?.let {
                val documents=it.documents
                for(document in documents)
                {
                    val photo=Gallery(document["galleryPhotoUrl"] as String,document["date"] as String?,document["firebaseDate"] as Timestamp,document["comment"] as String?)
                    //val photo=Gallery(document["profilePhotoUrl"] as String,null,null)
                    list.add(photo)
                }
                photo.value=list

            }
        }
    }

}