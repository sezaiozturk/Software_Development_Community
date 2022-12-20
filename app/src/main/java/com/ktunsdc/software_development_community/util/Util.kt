package com.ktunsdc.software_development_community.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ktunsdc.software_development_community.R

fun ImageView.downloadFromUrl(url:String?,gender:String?){
    var errorPhoto=0
    if(gender=="0")errorPhoto=R.mipmap.avatar
    else if(gender=="1") errorPhoto=R.mipmap.woman
    else errorPhoto=R.drawable.ic_baseline_person_add_24
    val options=RequestOptions()
        .placeholder(R.drawable.circular_progress_animation)
        .error(errorPhoto)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)

}
fun ImageView.downloadFromUrltoGallery(url:String?){
    val options=RequestOptions()
        .placeholder(R.drawable.circular_progress_animation)
        .error(R.drawable.ic_baseline_null_photo_24)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)

}

@BindingAdapter("android:downloadUrl","android:gender")
fun downloadImage(view: ImageView,url: String?,gender: String?){
    view.downloadFromUrl(url,gender)
}
@BindingAdapter("android:downloadUrltoGallery")
fun downloadImage2(view: ImageView,url: String?){
    view.downloadFromUrltoGallery(url)
}


@BindingAdapter("android:status")
fun setStatus(view: ImageView,status: Boolean){
   if(status)view.setImageResource(R.mipmap.check)
    else view.setImageResource(R.mipmap.clock)
}