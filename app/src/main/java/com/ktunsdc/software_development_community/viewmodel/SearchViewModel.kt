package com.ktunsdc.software_development_community.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.ktunsdc.software_development_community.model.Member
import com.ktunsdc.software_development_community.util.FirebaseFunctions
import com.ktunsdc.software_development_community.util.Singleton
import java.util.ArrayList

class SearchViewModel:ViewModel() {
    val memberList=MutableLiveData<ArrayList<Member>>()
    val load=MutableLiveData<Boolean>()
    val currentlevel=MutableLiveData<String>()
    val firebaseFunctions=FirebaseFunctions()

    fun getMembers(){
        load.value=true
        Singleton.db.collection("Members").get().addOnSuccessListener {
            val documents=it.documents
            memberList.value=firebaseFunctions.membersInfo(documents)
            load.value=false
        }
    }
    fun getFilterMembers(filter:String){
        Singleton.db.collection("Members").orderBy("name").startAt(filter).endAt(filter+'\uf8ff').get().addOnSuccessListener {
            val documents=it.documents
            memberList.value=firebaseFunctions.membersInfo(documents)
        }
    }
    fun getCurrentLevel(){
        Singleton.db.collection("Members").document(Singleton.auth.currentUser!!.uid).get().addOnSuccessListener {
            val level=it.get("level") as String
            currentlevel.value=level
        }
    }
}