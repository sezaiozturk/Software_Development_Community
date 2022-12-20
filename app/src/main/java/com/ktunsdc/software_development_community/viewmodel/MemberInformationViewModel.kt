package com.ktunsdc.software_development_community.viewmodel

import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.ktunsdc.software_development_community.model.Member
import com.ktunsdc.software_development_community.util.FirebaseFunctions
import com.ktunsdc.software_development_community.util.Singleton
import kotlinx.coroutines.newSingleThreadContext
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MemberInformationViewModel:ViewModel() {
    val currentUser=Singleton.auth.currentUser!!.uid
    var load=MutableLiveData<Boolean>()
    var error=MutableLiveData<Boolean>()
    var members=MutableLiveData<Member>()
    var login=MutableLiveData<Boolean>()
    var update=MutableLiveData<Boolean>()
    var department=MutableLiveData<ArrayList<String>>()
    var area=MutableLiveData<ArrayList<String>>()

    val uuid=UUID.randomUUID()
    val referance=Singleton.storage.reference.child("profilPhoto").child("${uuid}.jpeg")

    fun save(member: Member){
        load.value=true
        Singleton.db.collection("Members").whereEqualTo("userName",member.userName).get().addOnSuccessListener {
            if(it.isEmpty){
                Singleton.db.collection("Members").document(currentUser).set(member).addOnSuccessListener {document->
                    if(member.profilePhotoUrl!=null){
                        referance.putFile(member.profilePhotoUrl.toUri()).addOnSuccessListener {
                            referance.downloadUrl.addOnSuccessListener {
                                val downloadUrl=it.toString()
                                val photoHashMap= hashMapOf<String,Any>()
                                photoHashMap.put("profilePhotoUrl",downloadUrl)
                                Singleton.db.collection("Members").document(currentUser).set(photoHashMap,SetOptions.merge())
                            }
                        }
                    }
                    load.value=false
                    login.value=true
                }
            }
            else{
                load.value=false
                error.value=true
            }
        }

    }
    fun infoGet(){
        load.value=true
        Singleton.db.collection("Members").document(currentUser).get().addOnSuccessListener {document->
            document?.let {
                //val member= Member(it.get("uid") as String,it.get("profilePhotoUrl")as String,it.get("gender") as String,it.get("userName") as String,it.get("name") as String,it.get("surName") as String,it.get("department") as String,it.get("area") as String,it.get("clas") as String,it.get("account") as String,it.get("biography") as String,it.get("dateOfRegistration") as Timestamp,it.get("level") as Number,it.get("status")as Boolean)
                members.value=FirebaseFunctions().singleMemberInfo(it)
                load.value=false
            }
        }.addOnFailureListener {
            load.value=false
        }
    }
    fun getDepartment(){
        load.value=true
        var departmentList= arrayListOf<String>()
        Singleton.db.collection("Department").get().addOnSuccessListener {
            val documents=it.documents
            for(document in documents){
                departmentList.add(document["departmentName"] as String)
            }
            department.value=departmentList
            load.value=false

        }.addOnFailureListener {
            load.value=false
        }
    }
    fun getArea(){
        load.value=true
        var areaList= arrayListOf<String>()
        Singleton.db.collection("Area").get().addOnSuccessListener {
            val documents=it.documents
            for(document in documents){
                areaList.add(document["areaName"] as String)
            }
            area.value=areaList
            load.value=false

        }.addOnFailureListener {
            load.value=false
        }
    }

    fun update(member: Member) {
        load.value=true
        Singleton.db.collection("Members").whereNotEqualTo("uid",member.uid).get().addOnSuccessListener {
            val documents=it.documents
            var counter=0
            for(document in documents){
                if(member.userName==document.get("userName"))counter++
            }
            if(counter==0){
                Singleton.db.collection("Members").document(currentUser).set(member).addOnSuccessListener {document->
                    if(member.profilePhotoUrl!=null){
                        referance.putFile(member.profilePhotoUrl.toUri()).addOnSuccessListener {
                            referance.downloadUrl.addOnSuccessListener {
                                val downloadUrl=it.toString()
                                val photoHashMap= hashMapOf<String,Any>()
                                photoHashMap.put("profilePhotoUrl",downloadUrl)
                                Singleton.db.collection("Members").document(currentUser).set(photoHashMap,SetOptions.merge())
                            }
                        }
                    }
                    load.value=false
                    update.value=true
                }
            }
            else{
                load.value=false
                error.value=true
            }
            counter=0
        }
    }
}