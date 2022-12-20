package com.ktunsdc.software_development_community.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ktunsdc.software_development_community.util.Singleton

class SignupViewModel:ViewModel() {
    private val auth=Singleton.auth
    //var currentUser:MutableLiveData<FirebaseUser> = MutableLiveData(null)
    var user=MutableLiveData<Boolean>()
    var load=MutableLiveData<Boolean>()


    fun register(email:String,password:String){
        load.value=true
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
            it?.let {
                it.user!!.sendEmailVerification().addOnSuccessListener {
                    load.value=false
                    user.value=true
                }.addOnFailureListener {
                    load.value=false
                    user.value=false
                }
            }
        }.addOnFailureListener {
            load.value=false
            user.value=false
        }
    }
}