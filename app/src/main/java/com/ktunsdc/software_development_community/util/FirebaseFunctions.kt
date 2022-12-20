package com.ktunsdc.software_development_community.util

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.ktunsdc.software_development_community.model.Comment
import com.ktunsdc.software_development_community.model.Member
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FirebaseFunctions {
    var members=ArrayList<Member>()
    var comments=ArrayList<Any>()

    fun singleMemberInfo(document: DocumentSnapshot):Member{
        val uid=document.get("uid") as String
        val photoUrl=document.get("profilePhotoUrl")as String
        val gender=document.get("gender") as String
        val userName=document.get("userName") as String
        val name=document.get("name") as String
        val surName=document.get("surName") as String
        val department=document.get("department") as String
        val area=document.get("area") as String
        val clas=document.get("clas") as String
        val account=document.get("account") as String
        val biography=document.get("biography") as String
        val dateOfRegistration=document.get("dateOfRegistration") as Timestamp
        val level=document.get("level") as String
        val status=document.get("status")as Boolean
        val member= Member(uid,photoUrl,gender, userName, name, surName, department, area, clas, account, biography, dateOfRegistration, level, status)
        return member
    }
    fun membersInfo(documents:List<DocumentSnapshot>):ArrayList<Member>{
        members.clear()
        for (document in documents){
             members.add(singleMemberInfo(document))
        }
        return members
    }

    fun singleCommentInfo(document:DocumentSnapshot):Comment{
        val memberUid=document.get("memberUid") as String
        val memberName=document.get("memberName") as String
        val memberSurname=document.get("memberSurname") as String
        val memberPhotoUrl=document.get("memberPhotoUrl") as String
        val memberGender=document.get("memberGender") as String
        val postId=document.get("postId") as String
        val commentId=document.get("commentId") as String
        val firebaseDate=document.get("firebaseDate") as Timestamp
        val comment=document.get("comment") as String
        val lineId=document.get("lineId") as String
        val comments=Comment(memberUid,memberName,memberSurname,memberPhotoUrl,memberGender,postId,commentId,firebaseDate,comment,lineId)
        return comments
    }


    fun commentsInfo(documents:List<DocumentSnapshot>):ArrayList<Any>{
        comments.clear()
        for (document in documents){
            comments.add(singleCommentInfo(document))
        }
        return comments
    }
    fun sort(list:ArrayList<Any>):ArrayList<Any>{

        val number=list.size-1
        for(i in 0..number){
            var value=i
            var max=(list[i] as Comment).firebaseDate.seconds
            for (j in (i+1)..number){
                if ((list[j] as Comment).firebaseDate.seconds>max){
                    max=(list[j] as Comment).firebaseDate.seconds
                    value=j
                }
            }
            val temp=list[i]
            list[i]=list[value]
            list[value]=temp
        }
        return list
    }
    fun differentTime(time:Long):String{
        val currentTime=Timestamp.now().seconds
        val differentTime=currentTime-time
        if(differentTime<60){
            return "$differentTime saniye önce"
        }
        else if(differentTime<3600){
            return "${differentTime/60} dakika önce"
        }
        else if(differentTime<86400){
            return "${differentTime/3600} saat önce"
        }
        else if(differentTime<604800){
            return "${differentTime/86400} gün önce"
        }
        else if(differentTime<2629800){
            return "${differentTime/604800} hafta önce"
        }
        else if(differentTime<31557600){
            return "${differentTime/2629800} ay önce"
        }
        else{
            return "${differentTime/31557600} yıl önce"
        }
    }
    fun toHour(timestamp: Timestamp):String{
        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
        val sdf = SimpleDateFormat("h:mm a")
        val netDate = Date(milliseconds)
        val date = sdf.format(netDate).toString()
        return date

    }
}