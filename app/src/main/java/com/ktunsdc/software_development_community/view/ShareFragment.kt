package com.ktunsdc.software_development_community.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.ImageHeaderParser
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.databinding.FragmentShareBinding
import com.ktunsdc.software_development_community.util.CustomAlertDialog
import com.ktunsdc.software_development_community.viewmodel.ShareViewModel


class ShareFragment : Fragment() {

    private lateinit var binding:FragmentShareBinding
    private lateinit var topAppBar:MaterialToolbar
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedPhoto:Uri?=null
    private var activityDate:String?=null
    private lateinit var viewModel: ShareViewModel
    private val loadAlertDialog=CustomAlertDialog()
    private var control:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_share,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).binding.bottomNavigationView.visibility=View.GONE
        topAppBar=binding.materialToolbar
        binding.gallery=this
        viewModel=ViewModelProvider(this)[ShareViewModel::class.java]
        loadAlertDialog.create(this,R.layout.alert_dialog_loading)
        registerLauncher()
        loadObserve()
        resultObserve()
        topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        arguments?.let {
            control=ShareFragmentArgs.fromBundle(it).control
            if(control==1){
                binding.materialToolbar.title="FOTOĞRAF YAYINLA"
                binding.btnPhoto.text = "FOTOĞRAF YAYINLA"
            }
            if(control==0){
                binding.materialToolbar.title="DUYURU YAYINLA"
                binding.btnPhoto.text = "DUYURU YAYINLA"
                topAppBar.menu.findItem(R.id.date).isVisible=false
            }
        }
        topAppBar.setOnMenuItemClickListener { menuItem->
            when (menuItem.itemId) {
                R.id.date -> {
                    activityDate()
                    true
                }
                R.id.selectPhoto->{
                    selectPhoto()
                    true
                }
                else -> false
            }
        }
        binding.btnPhoto.setOnClickListener {
        }
    }

    fun selectPhoto(){
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(requireView(),"Galeriye gitmek için izin gerekli", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver"){
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }else{
                //request permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        else{
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .createIntent { intent ->
                    activityResultLauncher.launch(intent)
                }

        }
    }
    fun registerLauncher(){
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode== Activity.RESULT_OK){
                val intentFromResult=result.data
                if(intentFromResult!=null){
                    selectedPhoto=intentFromResult.data
                    selectedPhoto?.let {
                        binding.galleryPhoto.setImageURI(it)
                        selectedPhoto=it
                    }
                }
            }
        }
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                val intentToGallery= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
            else{
                Toast.makeText(context,"İzin gerekli", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun activityDate(){
        val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Etkinlik Tarihi")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePicker.show(parentFragmentManager,"Material_Date_Picker")
        datePicker.addOnPositiveButtonClickListener {
            activityDate=datePicker.headerText
        }
    }
    fun shareGallery(comment:String?){
        if(selectedPhoto!=null)
        {
            if(control==1) viewModel.shareGallery(selectedPhoto!!,activityDate,comment)
            else if(control==0) viewModel.sharePost(selectedPhoto!!,comment)
        }
        else Toast.makeText(context,"Lütfen bir resim ekleyiniz!!",Toast.LENGTH_LONG).show()
    }
    fun loadObserve(){
        viewModel.load.observe(viewLifecycleOwner){
            if(it) loadAlertDialog.open()
            else loadAlertDialog.close()
        }
    }
    fun resultObserve(){
        viewModel.result.observe(viewLifecycleOwner){
            if(it){
                binding.galleryPhoto.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24)
                binding.txtComment.text!!.clear()
                requireActivity().onBackPressed()
            }
            else Toast.makeText(context,"Beklenmedik hata!!",Toast.LENGTH_LONG).show()
        }
    }
}