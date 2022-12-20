package com.ktunsdc.software_development_community.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.ktunsdc.software_development_community.model.Comment
import com.ktunsdc.software_development_community.model.Member
import com.ktunsdc.software_development_community.model.Post
import com.ktunsdc.software_development_community.util.FirebaseFunctions
import com.ktunsdc.software_development_community.util.Singleton
import java.util.*
import kotlin.collections.ArrayList

class CommentViewModel:ViewModel() {
    var comment= MutableLiveData<ArrayList<Any>>()
    var load=MutableLiveData<Boolean>()
    var sendLoad=MutableLiveData<Boolean>()

    fun getComment(postId:String){
        load.value=true
        Singleton.db.collection("Comment").whereEqualTo("postId",postId).addSnapshotListener { value, error ->
            value?.let {
                val documents=it.documents
                var list=FirebaseFunctions().commentsInfo(documents)
                list=FirebaseFunctions().sort(list)
                Singleton.db.collection("Post").whereEqualTo("postId",postId).get().addOnSuccessListener {
                    it?.let {
                        val docs=it.documents
                        for(document in docs){
                            val post=Post(postId,document["memberUid"] as String,
                                document["postPhotoUrl"] as String,
                                document["firebaseDate"] as Timestamp,
                                document["comment"] as String,
                                document["lineId"] as String)
                            list.add(0,post)
                            list.add(1,"commentLine")
                        }
                        if(list.size==2) list.add("emptyLine")
                        comment.value=list
                    }
                }
            }
            load.value=false
        }
    }

    fun sendComment(comment: String, postId: String,currentMember:Member) {
        sendLoad.value=true
        val commentId=UUID.randomUUID().toString()
        val comments=Comment(currentMember.uid,currentMember.name,currentMember.surName,currentMember.profilePhotoUrl,currentMember.gender,postId,commentId, Timestamp.now(),comment,"1")
        Singleton.db.collection("Comment").add(comments).addOnSuccessListener {
            sendLoad.value=false
        }
    }
}