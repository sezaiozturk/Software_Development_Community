package com.ktunsdc.software_development_community.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ktunsdc.software_development_community.util.Singleton

class ForgotPasswordViewModel:ViewModel() {
    val load=MutableLiveData<Boolean>()
    val message=MutableLiveData<Boolean>()

    private val auth= Singleton.auth
    fun resetPassword(email:String){
        load.value=true
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            load.value=false
            message.value=true

        }.addOnFailureListener {
            load.value=false
            message.value=false

        }
    }
}