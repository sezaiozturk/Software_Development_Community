package com.ktunsdc.software_development_community.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.databinding.GalleryRecyclerRowPhotoBinding
import com.ktunsdc.software_development_community.model.Gallery
import com.ktunsdc.software_development_community.view.GalleryFragmentDirections

class GalleryAdapter(val galleryList:ArrayList<Gallery>):RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>(),ClickListener{
    class GalleryViewHolder(val binding:GalleryRecyclerRowPhotoBinding ):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val binding=DataBindingUtil.inflate<GalleryRecyclerRowPhotoBinding>(inflater, R.layout.gallery_recycler_row_photo,parent,false)
        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.binding.gallery=galleryList[position]
        holder.binding.rowPhoto.tag=position
        holder.binding.listener=this
    }

    override fun getItemCount(): Int {
        return galleryList.size
    }
    fun updateGallery(list:ArrayList<Gallery>){
        galleryList.clear()
        galleryList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onMemberClicked(v: View) {
        val line=v.tag
        val action= GalleryFragmentDirections.actionGalleryFragmentToPhotoFragment(line as Int)
        Navigation.findNavController(v).navigate(action)
    }
}