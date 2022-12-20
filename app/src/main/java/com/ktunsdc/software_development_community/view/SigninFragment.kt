package com.ktunsdc.software_development_community.view

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.databinding.FragmentSigninBinding
import com.ktunsdc.software_development_community.util.CustomAlertDialog
import com.ktunsdc.software_development_community.util.FormControl
import com.ktunsdc.software_development_community.util.Singleton
import com.ktunsdc.software_development_community.viewmodel.SigninViewModel


class SigninFragment : Fragment() {
    private lateinit var binding:FragmentSigninBinding
    private lateinit var viewModel:SigninViewModel
    private var loadingAlertDialog=CustomAlertDialog()
    private var auth: FirebaseAuth=Firebase.auth
    private var errorAlertDialog=CustomAlertDialog()
    private var formControl=FormControl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_signin,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signinFragment=this
        viewModel=ViewModelProvider(this)[SigninViewModel::class.java]
        loadingAlertDialog.create(this,R.layout.alert_dialog_loading)

        loadObserve()
        messageObserve()
        formControl.emailFocusListener(binding.txtMail,binding.layoutMail)
        formControl.passwordFocusListener(binding.txtPassword,binding.layoutPassword)


    }

    fun registerNow(){
        val action=SigninFragmentDirections.actionSigninFragmentToSignupFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
    fun signin(email:String,password:String){
        binding.layoutMail.helperText=formControl.validEmail(binding.txtMail)
        binding.layoutPassword.helperText=formControl.validPassword(binding.txtPassword)

        val validEmail=binding.layoutMail.helperText==null
        val validPassword=binding.layoutPassword.helperText==null
        if(validEmail&&validPassword){
            viewModel.signin(email,password)
        }
        else{
            Toast.makeText(context,"Lütfen alanları kontrol edin!!!",Toast.LENGTH_LONG).show()
        }
    }
    fun signinWithGoogle(){
        Toast.makeText(context,"Henüz aktif değil",Toast.LENGTH_LONG).show()
    }
    fun signinWithGithub(){
        Toast.makeText(context,"Henüz aktif değil",Toast.LENGTH_LONG).show()
    }
    fun forgotPassword(){
        val action=SigninFragmentDirections.actionSigninFragmentToForgotPasswordFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
    fun loadObserve(){
        viewModel.load.observe(viewLifecycleOwner){
            if(it==true){
                loadingAlertDialog.open()
            }else{
                loadingAlertDialog.close()
            }
        }
    }
    fun messageObserve(){
        viewModel.message.observe(viewLifecycleOwner){
            if(it.equals("toFragment")){
                val action=SigninFragmentDirections.actionSigninFragmentToMemberInformationFragment(0)
                this.findNavController().navigate(action)
            }else if(it.equals("toActivity")){
                (activity as LoginActivity).sharedPreferences.edit().putBoolean("memberInfo",true).apply()
                val intent=Intent(context,MainActivity::class.java)
                startActivity(intent)
                (activity as LoginActivity).finish()
            }
            else if(it.equals("verification")){
                Toast.makeText(context,"Lütfen önce e-mail adresinizi doğrulayın!!!",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context,"E-posta ve şifrenizi kontrol edin!!!",Toast.LENGTH_LONG).show()
            }
        }
    }
}