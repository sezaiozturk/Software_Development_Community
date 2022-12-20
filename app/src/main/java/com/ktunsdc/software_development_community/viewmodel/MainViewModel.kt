package com.ktunsdc.software_development_community.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.ktunsdc.software_development_community.model.Member
import com.ktunsdc.software_development_community.util.FirebaseFunctions
import com.ktunsdc.software_development_community.util.Singleton

class MainViewModel:ViewModel() {
    val load=MutableLiveData<Boolean>()
    val currentMember=MutableLiveData<Member>()

    fun getCurrentMember(){
        load.value=true
        Singleton.db.collection("Members").document(Singleton.auth.currentUser!!.uid).get().addOnSuccessListener {
            val member=FirebaseFunctions().singleMemberInfo(it)
            currentMember.value=member
            load.value=false
        }
    }
}