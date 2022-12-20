package com.ktunsdc.software_development_community.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.databinding.RecyclerRowPhotoBinding
import com.ktunsdc.software_development_community.model.Gallery

class PhotoAdapter(val photos:ArrayList<Gallery>):RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>(){
    class PhotoViewHolder(val binding: RecyclerRowPhotoBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<RecyclerRowPhotoBinding>(inflater, R.layout.recycler_row_photo,parent,false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.binding.photo=photos[position]
    }

    override fun getItemCount(): Int {
        return photos.size
    }
    fun updatePhotos(list:ArrayList<Gallery>){
        photos.clear()
        photos.addAll(list)
        notifyDataSetChanged()
    }
}