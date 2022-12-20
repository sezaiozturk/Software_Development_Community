package com.ktunsdc.software_development_community.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ktunsdc.software_development_community.util.Singleton
import com.ktunsdc.software_development_community.util.Singleton.auth

class SigninViewModel:ViewModel() {
    var load=MutableLiveData<Boolean>()
    var message=MutableLiveData<String>()


    fun signin(email:String,password:String){
        load.value=true
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            val currentuser= Singleton.auth.currentUser
            currentuser?.let {
                if(currentuser.isEmailVerified){
                    Singleton.db.collection("Members").document(currentuser.uid).get().addOnSuccessListener {
                        if(it.data==null){
                            load.value=false
                            message.value="toFragment"
                        }else{
                            load.value=false
                            message.value="toActivity"
                        }
                    }
                }
                else{
                    load.value=false
                    message.value="verification"
                }
            }
        }.addOnFailureListener {
            load.value=false
            message.value="control"
        }
    }
}