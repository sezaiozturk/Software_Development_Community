package com.ktunsdc.software_development_community.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.ktunsdc.software_development_community.model.Member
import com.ktunsdc.software_development_community.model.Message
import com.ktunsdc.software_development_community.util.Singleton
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatViewModel:ViewModel() {

    var load= MutableLiveData<Boolean>()
    var sendLoad= MutableLiveData<Boolean>()
    var messagelist=MutableLiveData<ArrayList<Message>>()

    fun sendMessage(message:String,member:Member){
        sendLoad.value=true
        val msgId=UUID.randomUUID().toString()
        val msg=Message(member.uid,member.name,member.surName,msgId,Timestamp.now(),message,"0")
        Singleton.db.collection("Chat").add(msg).addOnSuccessListener {
            sendLoad.value=false
        }
    }

    fun getMessage(){
        load.value=true
        Singleton.db.collection("Chat").orderBy("firebaseDate",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            value?.let {
                val documents=it.documents
                val list=ArrayList<Message>()
                for(document in documents){
                    val msg=Message(document.data!!["memberUid"] as String,
                        document.data!!["memberName"] as String,
                        document.data!!["memberSurname"] as String,
                        document.data!!["messageId"] as String,
                        document.data!!["firebaseDate"] as Timestamp,
                        document.data!!["message"] as String,
                        document.data!!["lineId"] as String)
                    list.add(msg)
                }
                messagelist.value=list
            }
            load.value=false
        }
    }
}