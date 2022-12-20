package com.ktunsdc.software_development_community.view

import android.icu.lang.UCharacter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.adapter.GalleryAdapter
import com.ktunsdc.software_development_community.databinding.FragmentGalleryBinding
import com.ktunsdc.software_development_community.util.Singleton
import com.ktunsdc.software_development_community.viewmodel.GalleryViewModel


class GalleryFragment : Fragment() {

    private lateinit var binding:FragmentGalleryBinding
    private lateinit var topAppBar:MaterialToolbar
    private lateinit var adapter: GalleryAdapter
    private lateinit var viewModel:GalleryViewModel
    private lateinit var mActivity:MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity=activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_gallery,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!mActivity.currentMember!!.status){
            val action=GalleryFragmentDirections.actionGalleryFragmentToLockFragment2()
            Navigation.findNavController(view).navigate(action)
        }
        else
        {
            (activity as MainActivity).binding.bottomNavigationView.visibility=View.VISIBLE
            val window: Window = (activity as MainActivity).window
            window.statusBarColor = ContextCompat.getColor((activity as MainActivity), R.color.blue)

            topAppBar=binding.materialToolbar
            viewModel=ViewModelProvider(this)[GalleryViewModel::class.java]
            viewModel.getPhotos()

            galleryObserve()
            swipeRefresh()

            adapter= GalleryAdapter(arrayListOf())
            binding.galleryRecycler.layoutManager=GridLayoutManager(context,3)
            binding.galleryRecycler.adapter=adapter

            if((activity as MainActivity).currentMember!!.level=="0"){
                topAppBar.menu.findItem(R.id.photo_share).isVisible=false
            }
            topAppBar.setOnMenuItemClickListener {
                if(it.itemId==R.id.photo_share){
                    val action=GalleryFragmentDirections.actionGalleryFragmentToShareFragment(1)
                    this.findNavController().navigate(action)
                }
                true
            }
            topAppBar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }

    }
    fun galleryObserve(){
        viewModel.photo.observe(viewLifecycleOwner){
            adapter.updateGallery(it)

        }
    }
    fun swipeRefresh(){
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getPhotos()
            binding.refreshLayout.isRefreshing = false
        }
    }
}