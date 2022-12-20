package com.ktunsdc.software_development_community.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.adapter.PhotoAdapter
import com.ktunsdc.software_development_community.databinding.FragmentPhotoBinding
import com.ktunsdc.software_development_community.viewmodel.PhotoViewmodel

class PhotoFragment : Fragment() {
    private lateinit var binding:FragmentPhotoBinding
    private lateinit var adapter: PhotoAdapter
    private lateinit var viewModel: PhotoViewmodel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_photo,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).binding.bottomNavigationView.visibility=View.GONE
        val window: Window = (activity as MainActivity).window
        window.statusBarColor = ContextCompat.getColor((activity as MainActivity), R.color.black)

        viewModel=ViewModelProvider(this)[PhotoViewmodel::class.java]
        adapter= PhotoAdapter(arrayListOf())

        binding.viewpagerPhoto.adapter=adapter


        photoListObserve()
        viewModel.getPhotos()
    }
    fun photoListObserve(){
        viewModel.photoList.observe(viewLifecycleOwner){
            it?.let {
                adapter.updatePhotos(it)
                arguments?.let {
                    binding.viewpagerPhoto.setCurrentItem(PhotoFragmentArgs.fromBundle(it).lineNumber,false)
                }
            }
        }
    }
}