package com.ktunsdc.software_development_community.view

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.number.IntegerWidth
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.toObject
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.databinding.FragmentMemberInformationBinding
import com.ktunsdc.software_development_community.model.Member
import com.ktunsdc.software_development_community.util.*
import com.ktunsdc.software_development_community.viewmodel.MemberInformationViewModel

class MemberInformationFragment : Fragment() {
    private lateinit var binding:FragmentMemberInformationBinding
    private lateinit var viewModel: MemberInformationViewModel
    private lateinit var activityResultLauncher:ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val loadingAlertDialog=CustomAlertDialog()
    private val welcomeAlertDialog=CustomAlertDialog()
    private val formControl=FormControl()
    var selectedPhoto:Uri?=null
    lateinit var gender:String
    private var getStatus=false
    private var getlevel="0"
    private var getDateOfRegistration=Timestamp.now()
    private var control:Int?=null
    lateinit var member:Member

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_member_information,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(this)[MemberInformationViewModel::class.java]
        binding.memberFragment=this
        val topAppBar=binding.materialToolbar

        loadingAlertDialog.create(this,R.layout.alert_dialog_loading)
        welcomeAlertDialog.create(this,R.layout.alert_dialog_welcome)
        registerLauncher()
        formControl()
        runObserve()
        combobox()
        topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.getDepartment()
        viewModel.getArea()

        arguments?.let {
            control=MemberInformationFragmentArgs.fromBundle(it).control
            if(control==1){
                infoGet()
                (activity as MainActivity).binding.bottomNavigationView.visibility=View.GONE
            }
            /*if(control==0){

            }else if(control==1){
                infoGet()
                (activity as MainActivity).binding.bottomNavigationView.visibility=View.GONE
            }*/
        }
    }

    private fun runObserve() {
        loadObserve()
        errorObserve()
        loginObserve()
        memberObserve()
        updateObserve()
        areaObserve()
        departmentObserve()
    }

    fun formControl(){
        formControl.textFocusListener(binding.txtUserName,binding.layoutUserName)
        formControl.textFocusListener(binding.txtName,binding.layoutName)
        formControl.textFocusListener(binding.txtSurname,binding.layoutSurname)
        formControl.autoTextFocusListener(binding.txtDepartment,binding.layoutDepartment)
        formControl.autoTextFocusListener(binding.txtArea,binding.layoutArea)
        formControl.autoTextFocusListener(binding.txtClass,binding.layoutClass)
        formControl.textFocusListener(binding.txtProfile,binding.layoutProfile)
    }
    fun saveFormControl(){
        binding.layoutUserName.helperText=formControl.validText(binding.txtUserName)
        binding.layoutName.helperText=formControl.validText(binding.txtName)
        binding.layoutSurname.helperText=formControl.validText(binding.txtSurname)
        binding.layoutDepartment.helperText=formControl.validAutoText(binding.txtDepartment)
        binding.layoutArea.helperText=formControl.validAutoText(binding.txtArea)
        binding.layoutClass.helperText=formControl.validAutoText(binding.txtClass)
        binding.layoutProfile.helperText=formControl.validText(binding.txtProfile)
        if(binding.radioWoman.isChecked)gender="1"
        else gender="0"
    }
    fun save(username:String,name:String,surname:String,department:String,area:String,clas:String,account:String,profile:String){
        saveFormControl()
        val uid=Singleton.auth.currentUser!!.uid
        val validUserName=binding.layoutUserName.helperText==null
        val validName=binding.layoutName.helperText==null
        val validSurName=binding.layoutSurname.helperText==null
        val validDepartment=binding.layoutDepartment.helperText==null
        val validArea=binding.layoutArea.helperText==null
        val validClass=binding.layoutClass.helperText==null
        val validProfile=binding.layoutProfile.helperText==null
        if(validUserName&&validName&&validSurName&&validDepartment&&validArea&&validClass/*&&validAccount*/&&validProfile){
            member=Member(uid,selectedPhoto.toString(),gender,username.filter { !it.isWhitespace() },name.replaceFirstChar {it.uppercase() },surname.replaceFirstChar {it.uppercase() },department,area,clas,account,profile,getDateOfRegistration,getlevel,getStatus)
            if(control==0){
                viewModel.save(member)
            }
            else if(control==1){
                viewModel.update(member)
            }
        }
        else{
            Toast.makeText(context,"Lüffen gerekli alanları doldurunuz!!!", Toast.LENGTH_LONG).show()
        }

    }
    fun selectPhoto(){
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(requireView(),"Galeriye gitmek için izin gerekli",Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver"){
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }else{
                //request permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        else{
            //intent to gallery
            val intentToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }
    fun registerLauncher(){
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if(result.resultCode==RESULT_OK){
                val intentFromResult=result.data
                if(intentFromResult!=null){
                    selectedPhoto=intentFromResult.data
                    selectedPhoto?.let {
                        println("girdi")
                        binding.memberPhoto.setImageURI(it)
                    }
                }
            }
        }
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                val intentToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
            else{
                Toast.makeText(context,"İzin gerekli",Toast.LENGTH_LONG).show()
            }
        }
    }
    fun loadObserve(){
        viewModel.load.observe(viewLifecycleOwner){
            if(it) loadingAlertDialog.open()
            else loadingAlertDialog.close()
        }
    }
    fun errorObserve(){
        viewModel.error.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(context,"Bu kullanıcı adı daha önce alınmış",Toast.LENGTH_LONG).show()
                binding.layoutUserName.helperText="farklı bir kullanıcı adı deneyin"
            }
        }
    }
    fun memberObserve(){
        viewModel.members.observe(viewLifecycleOwner){
            binding.memberModel=it
            getlevel=it.level
            getStatus=it.status
            getDateOfRegistration=it.dateOfRegistration
            it.profilePhotoUrl?.let {
                selectedPhoto=it.toUri()
            }
        }
    }
    fun loginObserve(){
        viewModel.login.observe(viewLifecycleOwner){
            if(it){
                welcomeAlertDialog.open()
                welcomeAlertDialog.button(R.id.btnWelcome).setOnClickListener {
                    welcomeAlertDialog.close()
                    (activity as LoginActivity).sharedPreferences.edit().putBoolean("memberInfo",true).apply()
                    val intent= Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    (activity as LoginActivity).finish()
                }

            }
        }
    }
    fun updateObserve(){
        viewModel.update.observe(viewLifecycleOwner){
            if(it){
                (activity as MainActivity).currentMember=member
                requireActivity().onBackPressed()
            }
        }
    }
    fun departmentObserve(){
        viewModel.department.observe(viewLifecycleOwner){
            it?.let {
                val adapterDepartment = ArrayAdapter(requireContext(), R.layout.list_item, it)
                (binding.txtDepartment as? AutoCompleteTextView)?.setAdapter(adapterDepartment)
            }
        }
    }
    fun areaObserve(){
        viewModel.area.observe(viewLifecycleOwner){
            it?.let {
                val adapterAreas = ArrayAdapter(requireContext(), R.layout.list_item, it)
                (binding.txtArea as? AutoCompleteTextView)?.setAdapter(adapterAreas)
            }
        }
    }

     fun infoGet() {
        viewModel.infoGet()
    }
     fun combobox(){
        val departments = listOf("Bilgisayar Mühendisliği", "Yazılım Mühendisliği")
        val areas = listOf("Web Geliştirme", "Mobil Programlama")
        val clas = listOf("1.Sınıf","2.Sınıf","3.Sınıf","4.Sınıf")


       /* val adapterDepartment = ArrayAdapter(requireContext(), R.layout.list_item, departments)
        (binding.txtDepartment as? AutoCompleteTextView)?.setAdapter(adapterDepartment)
        val adapterAreas = ArrayAdapter(requireContext(), R.layout.list_item, areas)
        (binding.txtArea as? AutoCompleteTextView)?.setAdapter(adapterAreas)*/
        val adapterClas = ArrayAdapter(requireContext(), R.layout.list_item, clas)
        (binding.txtClass as? AutoCompleteTextView)?.setAdapter(adapterClas)
    }
}