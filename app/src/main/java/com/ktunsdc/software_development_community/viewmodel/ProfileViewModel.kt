package com.ktunsdc.software_development_community.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ktunsdc.software_development_community.model.Member
import com.ktunsdc.software_development_community.util.FirebaseFunctions
import com.ktunsdc.software_development_community.util.Singleton

class ProfileViewModel:ViewModel() {
    var user= MutableLiveData<Member>()

    fun getUser(userUid:String){
        Singleton.db.collection("Members").document(userUid).get().addOnSuccessListener {document->
            document?.let {
                user.value=FirebaseFunctions().singleMemberInfo(it)
            }
        }
    }
}