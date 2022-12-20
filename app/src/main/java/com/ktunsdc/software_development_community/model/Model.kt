package com.ktunsdc.software_development_community.model

import com.google.firebase.Timestamp
import java.io.Serializable

data class Member(val uid:String,
                  val profilePhotoUrl: String?,
                  val gender:String,
                  val userName:String,
                  val name:String,
                  val surName:String,
                  val department:String,
                  val area:String,
                  val clas:String,
                  val account:String?,
                  val biography:String,
                  val dateOfRegistration:Timestamp,
                  val level:String,
                  val status:Boolean,)

data class Gallery(val galleryPhotoUrl:String,
                   val date:String?,
                   val firebaseDate:Timestamp,
                   val comment:String?)

data class Post(val postId:String,
                val memberUid:String,
                val postPhotoUrl:String,
                val firebaseDate: Timestamp,
                val comment: String?,
                val lineId:String):Serializable

data class Comment(val memberUid:String,
                   val memberName:String,
                   val memberSurname:String,
                   val memberPhotoUrl:String?,
                   val memberGender:String,
                   val postId:String,
                   val commentId:String,
                   val firebaseDate: Timestamp,
                   val comment: String,
                   val lineId:String)

data class Message(val memberUid:String,
                   val memberName:String,
                   val memberSurname: String,
                   val messageId:String,
                   val firebaseDate: Timestamp,
                   val message: String,
                   val lineId:String)

