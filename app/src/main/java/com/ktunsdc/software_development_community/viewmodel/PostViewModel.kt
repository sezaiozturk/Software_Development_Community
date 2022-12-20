package com.ktunsdc.software_development_community.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.ktunsdc.software_development_community.model.Post
import com.ktunsdc.software_development_community.util.Singleton

class PostViewModel:ViewModel() {
    var post=MutableLiveData<ArrayList<Any>>()

    fun getPost(){
        val list=ArrayList<Any>()
        Singleton.db.collection("Post").orderBy("firebaseDate", Query.Direction.DESCENDING).get().addOnSuccessListener {
            it?.let {
                val documents=it.documents
                for(document in documents)
                {
                    if(document["lineId"]=="0"){
                        val post= Post(document["postId"] as String,document["memberUid"] as String,document["postPhotoUrl"] as String,document["firebaseDate"] as Timestamp,document["comment"] as String?,document["lineId"] as String)
                        //val photo=Gallery(document["profilePhotoUrl"] as String,null,null)
                        list.add(post)
                    }
                }
                post.value=list
            }
        }
    }
}