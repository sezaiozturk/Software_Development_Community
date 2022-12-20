package com.ktunsdc.software_development_community.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.appbar.MaterialToolbar
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.databinding.FragmentSignupBinding
import com.ktunsdc.software_development_community.util.CustomAlertDialog
import com.ktunsdc.software_development_community.util.FormControl
import com.ktunsdc.software_development_community.viewmodel.SignupViewModel

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel:SignupViewModel
    private val loadingAlertDialog=CustomAlertDialog()
    private val verificationAlertDialog=CustomAlertDialog()
    private val errorAlertDialog=CustomAlertDialog()
    private val formControl=FormControl()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_signup,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupFragment=this
        viewModel= ViewModelProvider(this)[SignupViewModel::class.java]
        loadingAlertDialog.create(this,R.layout.alert_dialog_loading)
        verificationAlertDialog.create(this,R.layout.alert_dialog_verification)
        errorAlertDialog.create(this,R.layout.alert_dialog_error)

        loadObserve()
        currentUserObserve(view)

        formControl.emailFocusListener(binding.txtEmail,binding.layoutMail)
        formControl.passwordFocusListener(binding.txtPassword,binding.layoutPassword)
    }

    fun signup(email:String,password:String){
        binding.layoutMail.helperText=formControl.validEmail(binding.txtEmail)
        binding.layoutPassword.helperText=formControl.validPassword(binding.txtPassword)

        val validEmail=binding.layoutMail.helperText==null
        val validPassword=binding.layoutPassword.helperText==null
        if(validEmail&&validPassword){
            if(binding.txtPassword.text.toString()==binding.txtPassword2.text.toString()){
                viewModel.register(email,password)
            }else{
                Toast.makeText(context,"Şifreler aynı değil!!!", Toast.LENGTH_LONG).show()
            }

        }else{
            Toast.makeText(context,"Lütfen alanları kontrol edin!!!", Toast.LENGTH_LONG).show()
        }

    }
    fun toSignin(){
        val action=SignupFragmentDirections.actionSignupFragmentToSigninFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
    fun loadObserve(){
        viewModel.load.observe(viewLifecycleOwner){
            if(it){
                loadingAlertDialog.open()
            }else{
                loadingAlertDialog.close()
            }
        }
    }
    fun currentUserObserve(view: View){
        viewModel.user.observe(viewLifecycleOwner){
            if(it){
                verificationAlertDialog.open()
                verificationAlertDialog.button(R.id.btnVerification).setOnClickListener {
                    verificationAlertDialog.close()
                    val action=SignupFragmentDirections.actionSignupFragmentToSigninFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }else{
                errorAlertDialog.open()
                errorAlertDialog.button(R.id.btnError).setOnClickListener {
                    errorAlertDialog.close()
                }
            }
        }
    }
}